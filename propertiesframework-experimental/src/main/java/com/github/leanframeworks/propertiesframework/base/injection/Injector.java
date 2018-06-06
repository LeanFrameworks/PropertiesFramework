/*-
 * #%L
 * PropertiesFramework :: Experiments
 * %%
 * Copyright (C) 2017 LeanFrameworks
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package com.github.leanframeworks.propertiesframework.base.injection;

import com.github.leanframeworks.propertiesframework.api.property.ReadableProperty;
import com.github.leanframeworks.propertiesframework.api.property.ReadableWritableProperty;
import com.github.leanframeworks.propertiesframework.api.property.WritableProperty;
import com.github.leanframeworks.propertiesframework.base.action.RunnableWrapper;
import com.github.leanframeworks.propertiesframework.base.injection.annotation.ExposeA;
import com.github.leanframeworks.propertiesframework.base.injection.annotation.ExposeRO;
import com.github.leanframeworks.propertiesframework.base.injection.annotation.ExposeRW;
import com.github.leanframeworks.propertiesframework.base.injection.annotation.ExposeWO;
import com.github.leanframeworks.propertiesframework.base.injection.annotation.PostInject;
import com.github.leanframeworks.propertiesframework.base.injection.annotation.Require;
import com.github.leanframeworks.propertiesframework.base.injection.converter.CompositeConverter;
import com.github.leanframeworks.propertiesframework.base.injection.converter.Converter;
import com.github.leanframeworks.propertiesframework.base.injection.converter.DefaultActionConverter;
import com.github.leanframeworks.propertiesframework.base.injection.converter.DefaultPropertyConverter;
import com.github.leanframeworks.propertiesframework.base.injection.impl.AnnotatedField;
import com.github.leanframeworks.propertiesframework.base.injection.impl.AnnotatedMethod;
import com.github.leanframeworks.propertiesframework.base.injection.impl.AnnotatedMethodWithId;
import com.github.leanframeworks.propertiesframework.base.injection.impl.ExposedField;
import com.github.leanframeworks.propertiesframework.base.injection.impl.ExposedMethod;
import com.github.leanframeworks.propertiesframework.base.injection.impl.Owner;
import com.github.leanframeworks.propertiesframework.base.injection.impl.PostInjectMethod;
import com.github.leanframeworks.propertiesframework.base.injection.impl.ReflectionUtils;
import com.github.leanframeworks.propertiesframework.base.injection.impl.RequiredField;
import com.github.leanframeworks.propertiesframework.base.property.wrap.ReadOnlyPropertyWrapper;
import com.github.leanframeworks.propertiesframework.base.property.wrap.ReadWritePropertyWrapper;
import com.github.leanframeworks.propertiesframework.base.property.wrap.WriteOnlyPropertyWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.of;

/**
 * CONCEPTS
 * ========
 * <p>
 * Classes can expose and require properties and actions. Required properties and actions will be injected with the
 * properties and actions exposed by other classes.
 * <p>
 * Properties can be exposed with read-only, write-only or read-write access, using the {@link ExposeRO},
 * {@link ExposeWO} or {@link ExposeRW} respectively.
 * <p>
 * In order to expose or require properties and actions, the classes or class instances that expose or require them must
 * be registered using {@link #registerClass(Class)}, {@link #registerInstanceOfClass(Class)} or {@link
 * #registerInstance(Object)}.
 * <p>
 * {@link #registerInstanceOfClass(Class)} will instantiate the specified class automatically.
 * <p>
 * A non-instantiable class containing exposed/required fields can be registered as well. But only static fields will be
 * considered.
 * <p>
 * An already-instantiated object can also be registered (using {@link #registerInstance(Object)}. This allows custom
 * instantiation and makes it possible to mix with other instantiation/injection mechanisms/frameworks (Spring, JavaFX
 * FXML, etc.). In order not conflict with other injection frameworks, the standard {@link
 * javax.annotation.PostConstruct} annotation is not used. Another equivalent annotation is provided.
 * <p>
 * Registration steps:
 * 1. Instantiation: the given class is instantiated once;
 * 2. Discovery: all exposed and required static and non-static fields of the class and super classes are discovered;
 * 3. Injection: all unresolved required fields (all previous registrations) are injected as much as possible;
 * 4. Post-injection: the methods annotated with {@link PostInject} are called once fully injected.
 * <p>
 * If exception is thrown during the field discovery, the whole registration will fail. There is no partial
 * registration.
 * <p>
 * A same field can be exposed multiple times under different IDs and constraints (RO, WO, RW).
 * A same ID cannot be used multiple times to expose fields.
 * As a result, only one instance per class containing exposed fields can be registered. Several instances of a same
 * class can be registered if the class contains only required fields.
 * <p>
 * Exposed and required fields can be static or non-static.
 * <p>
 * Exposed fields should be final and non-null.
 * Required fields should not be final, but should be null.
 * <p>
 * The required type can be deducted from the declaration. But the exposed type should not be deducted because it would
 * require redundant property declarations (for example, one RW property for internal logic, and one RO wrapper property
 * to be exposed). So the -RO, -WO and -RW suffixes are needed for Expose but not for Require.
 * <p>
 * The injection framework should not allow too much to avoid dirty things. So it is for now restricted to properties
 * and not any kind of POJO.
 * <p>
 * APPLICATION TO THE MVP PATTERN
 * ==============================
 * <p>
 * This mechanism can be used for instance to implement the MVP pattern where the presenter(s) and model(s) are loosely
 * couple. Models typically expose properties and actions, while presenters typically require properties and actions.
 * <p>
 * As a result, the presenters and the models would not need any explicit reference to each. The models do not need to
 * know about the presenters; the presenters do not need to know about the models and from which model the properties
 * and actions come from. The models can then evolve and be refactored and decomposed as needed when the complexity of
 * the application grows, without impacting the presenters, as long as the properties and actions remain exposed with
 * compatible access rights.
 * <p>
 * An example of the passive view MVP would be the following. One or several models would contain the abstraction of the
 * whole UI state, and expose it with properties. The models would also expose actions that should be triggered by the
 * presenter(s). One or several presenters get model properties and actions injected. They would merely configure the
 * view (UI components) and do the bindings to map the model properties to the views and the view events (mouse clicks,
 * keyboard input, touch input, etc.) to the model actions.
 * <p>
 * In the case of a JavaFX application, the implementation would remain minimal. The presenter(s) would get the UI
 * components injected (with the {@link javafx.fxml.FXML} annotation), as well as the model properties and actions.
 * <p>
 * This structure would allow the whole application logic to be implemented in the models and the layers below, and
 * completely independent from the technology used for the view (Swing, JavaFX, etc.). This maximizes the use of unit
 * tests to test the whole application logic.
 * <p>
 * QUESTIONS
 * =========
 * <p>
 * TODO Question: how to make it more type safe? at compilation time and at injection time (generics)?
 * TODO Question: throw exceptions or log errors? make it configurable?
 * TODO Question: strict mode (throwing exceptions) to be less error prone or log warning to be more flexible?
 * <p>
 * SHORT TERM
 * ==========
 * <p>
 * TODO Use optional enum to specify constraint on @Exposed (less convenient to type) instead of using  different annotations
 * TODO Auto-deduct exposed type based on declaration
 * TODO See other TODOs in the code
 * TODO Getter to get an exposed field (new wrapper instance)
 * TODO Exposed action can be method, Runnable, ActionListener, or any functional interface, with 0 or 1 parameter, static/non-static
 * TODO Post-construct should be static for class registration
 * TODO Post-construct can be static for instance registration
 * TODO Support for JavaFX properties (ObservableValue, ObservableBooleanValue, etc.)
 * TODO Can exposed only if fully resolved (to avoid conditional logic from W properties on an incomplete instance)
 * TODO Warning for classes with no exposed/required properties
 * TODO Multiple register with(-out) required, exposed, postinject
 * <p>
 * MEDIUM TERM
 * ===========
 * <p>
 * TODO Use reflection for actions (@Expose on methods)
 * TODO Better handler multiple registration of a same class/instance (with static fields)
 * TODO Decouple from JavaFX and make it modular/pluggable
 * TODO Decouple from VF/PF and make it even more modular
 * TODO Dispose/forget/allow GC with pre-destroy
 * TODO Priority and circular dependency
 * TODO Disallow instantiation if constructor is private?
 * TODO Warning for unrequired exposed? Also for ExposeRW but only Require as RO or WO?
 * TODO Allow same ID for exposed properties and actions because they do not technically conflict?
 * <p>
 * LONG TERM
 * =========
 * <p>
 * TODO Detect when writable property inject multiple times? optional?
 * TODO Bind instead of injecting? (reduces the need for PostInject methods)
 * TODO Be able to expose a property as multiple types but using a single ID
 * TODO Support self-inject of self-exposed fields?
 * TODO Support for re-exposed of injected field (under another name or other constraints)?
 * TODO Support any type for Expose/Require? (suffixes for Require no longer needed)
 * TODO Support constructor injection? Does it even make sense?
 * TODO Pass instance factory/function to instantiate when registering?
 * TODO Pass annotation for post-construct when registering?
 * TODO More than 1 parameter for actions?
 * TODO Thread-safe
 * TODO Cross-thread injection/binding
 * TODO OSGi support
 * TODO How instantiate non-static inner class?
 * TODO Exposed property can be initialized in post-construct method (delayed exposure, but /!\ late mistake discovery)
 */
public class Injector {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Injector.class);

    /**
     * Keep all instances of all registered classes instantiated by this injector.
     * <p>
     * This prevents them from being garbage collected.
     */
    protected final Map<Class<?>, Collection<Object>> classToInstances = new HashMap<>();

    protected final Map<String, ExposedField> idToExposedFields = new HashMap<>();

    protected final Map<String, ExposedMethod> idToExposedMethods = new HashMap<>();

    protected final Map<Owner, Collection<RequiredField>> ownerToRequiredFields = new HashMap<>();

    protected final Map<Owner, Collection<RequiredField>> ownerToUnresolvedRequiredFields = new HashMap<>();

    protected final Map<Owner, List<PostInjectMethod>> ownerToPostInjectMethods = new HashMap<>();

    // TODO Reverse order? (last added overrides)
    protected final CompositeConverter converters = new CompositeConverter();

    public Injector() {
        // Default converters
        addConverter(new DefaultPropertyConverter());
        addConverter(new DefaultActionConverter());
    }

    protected void dumpExposed() {
        idToExposedFields.values().forEach(System.out::println);
    }

    /**
     * Verifies that all fields annotated as required have been injected.
     *
     * @return True if all required fields have been injected, false otherwise.
     */
    public boolean verify() {
        boolean everythingInjected = ownerToUnresolvedRequiredFields.isEmpty();

        if (!everythingInjected) {
            StringBuilder sb = new StringBuilder();
            sb.append("The following fields have not yet been injected:\n");
            ownerToUnresolvedRequiredFields.values().forEach(
                    rs -> rs.forEach(r -> sb.append(r.getLogString()).append('\n'))
            );
            LOGGER.error(sb.toString());
        }

        return everythingInjected;
    }

    public void addConverter(Converter converter) {
        converters.addConverter(converter);
    }

    public void removeConverter(Converter converter) {
        converters.removeConverter(converter);
    }

    /**
     * Registers the specified class.
     * <p>
     * The class will not be instantiated. All static fields exposed by this class, will be subject to injection in
     * other registered instances or classes.
     *
     * @param clazz Class to be registered.
     * @see #registerInstanceOfClass(Class)
     * @see #registerInstance(Object)
     */
    public void registerClass(Class<?> clazz) {
        registerOwner(new Owner(clazz));
    }

    /**
     * Instantiates the specified class and registers the newly created instances.
     * <p>
     * All fields exposed by the newly created instance, as well as all static fields exposed by its class, will be
     * subject to injection in other registered instances or classes.
     *
     * @param clazz Class to be instantiated and whose instance should be registered.
     * @param <T>   Type of object to be instantiated and registered.
     * @return Instantiated and registered object.
     * @see #registerClass(Class)
     * @see #registerInstance(Object)
     */
    public <T> T registerInstanceOfClass(Class<T> clazz) {
        return registerInstance(instantiateClass(clazz));
    }

    /**
     * Registers the specified object.
     * <p>
     * All fields exposed by this instance, as well as all static fields exposed by its class, will be subject to
     * injection in other registered instances or classes.
     * <p>
     * This method can be useful in case you instantiated the class yourself or
     *
     * @param instance Object to be registered.
     * @param <T>      Type of object to be registered.
     * @return Registered object passed as argument (for convenience).
     * @see #registerClass(Class)
     * @see #registerInstanceOfClass(Class)
     */
    public <T> T registerInstance(T instance) {
        return (T) registerOwner(new Owner(instance)).getOwnerInstance();
    }

    /**
     * Registers the specified owner (class or instance of a class)
     * <p>
     * All fields exposed by this owner, static or not, will be subject to injection in other registered instances or
     * classes.
     *
     * @param owner Owner to be registered.
     * @return Registered owner passed as argument (for convenience).
     */
    private Owner registerOwner(Owner owner) {
        // Discover exposed and required static/non-static fields
        Map<String, ExposedField> exposedFields = discoverExposedFields(owner);
        Map<String, ExposedMethod> exposedMethods = discoverExposedMethods(owner);
        Collection<RequiredField> requiredFields = discoverRequiredFields(owner);
        List<PostInjectMethod> postInjectMethod = discoverPostInjectMethods(owner);

        // Store everything only if no exception was thrown
        if (owner.getOwnerInstance() != null) {
            Collection<Object> instances = classToInstances.computeIfAbsent(owner.getOwnerClass(),
                    k -> new HashSet<>());
            instances.add(owner.getOwnerInstance());
        }
        if ((exposedMethods != null) && !exposedMethods.isEmpty()) {
            idToExposedMethods.putAll(exposedMethods);
        }
        if ((exposedFields != null) && !exposedFields.isEmpty()) {
            idToExposedFields.putAll(exposedFields);
        }
        if ((requiredFields != null) && !requiredFields.isEmpty()) {
            ownerToRequiredFields.put(owner, requiredFields);
            ownerToUnresolvedRequiredFields.put(owner, requiredFields);
        }
        if ((postInjectMethod != null) && !postInjectMethod.isEmpty()) {
            ownerToPostInjectMethods.put(owner, postInjectMethod);
        }

        // Inject everything in all pending owners
        Collection<Owner> fullyInjectedOwners = injectUnresolvedRequiredFields();

        // Call PostInject method of pending owners if all fields are now injected
        callPostInjectMethods(fullyInjectedOwners, owner);

        return owner;
    }

    /**
     * Instantiates the specified class.
     *
     * @param clazz Class to be instantiated.
     * @param <T>   Type of class to be instantiated.
     * @return Instantiated object.
     */
    private <T> T instantiateClass(Class<T> clazz) {
        T instance;

        // Instantiate
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            boolean wasAccessible = constructor.isAccessible();
            constructor.setAccessible(true);

            try {
                instance = (T) constructor.newInstance();
            } catch (Exception e) {
                // Catch all checked exceptions (from the tried method) and unchecked exception that might occur in the
                // constructor of the instantiated class
                throw new InstantiationException(e);
            } finally {
                constructor.setAccessible(wasAccessible);
            }
        } catch (NoSuchMethodException e) {
            throw new InstantiationException(e);
        }

        return instance;
    }

    /**
     * Retrieves all fields exposed by the specified instance of the specified class.
     *
     * @param owner Class and instance owning the fields.
     * @return Mapping between IDs and exposed fields.
     */
    private Map<String, ExposedField> discoverExposedFields(Owner owner) {
        Map<String, ExposedField> exposedByThisClass = new HashMap<>();

        if ((owner.getOwnerClass() != null) && !Object.class.equals(owner.getOwnerClass())) {
            // Get fields exposed in parent class
            exposedByThisClass.putAll(discoverExposedFields(
                    new Owner(owner.getOwnerClass().getSuperclass(), owner.getOwnerInstance())));

            // Get fields exposed in this class
            for (Field field : owner.getOwnerClass().getDeclaredFields()) {
                for (ExposeRO ero : field.getAnnotationsByType(ExposeRO.class)) {
                    discoverExposedField(new AnnotatedField(owner, field, ero, ero.value()), exposedByThisClass);
                }
                for (ExposeWO ewo : field.getAnnotationsByType(ExposeWO.class)) {
                    discoverExposedField(new AnnotatedField(owner, field, ewo, ewo.value()), exposedByThisClass);
                }
                for (ExposeRW erw : field.getAnnotationsByType(ExposeRW.class)) {
                    discoverExposedField(new AnnotatedField(owner, field, erw, erw.value()), exposedByThisClass);
                }
                for (ExposeA e : field.getAnnotationsByType(ExposeA.class)) {
                    discoverExposedField(new AnnotatedField(owner, field, e, e.value()), exposedByThisClass);
                }
            }
        }

        return exposedByThisClass;
    }

    private void discoverExposedField(AnnotatedField annotated, Map<String, ExposedField> exposedByThisClass) {
        if ((annotated.getOwnerInstance() != null) || Modifier.isStatic(annotated.getField().getModifiers())) {
            // Some checks
            ensureExposedIsFinal(annotated);
            ensureIDIsNotAlreadyUsed(annotated, exposedByThisClass);

            // Get value
            Object value;
            boolean wasAccessible = annotated.getField().isAccessible();
            annotated.getField().setAccessible(true);
            try {
                value = annotated.getField().get(annotated.getOwnerInstance());
            } catch (IllegalAccessException e) {
                // TODO Better message
                throw new DiscoveryException(e);
            } finally {
                annotated.getField().setAccessible(wasAccessible);
            }

            // More checks
            ExposedField exposed = new ExposedField(annotated, value);
            ensureExposedIsOfCompatibleType(exposed);

            // Store field
            exposedByThisClass.put(annotated.getId(), exposed);
        } else {
            LOGGER.warn("{}, will be ignored because it is not static", annotated.getLogString());
        }
    }

    private void ensureExposedIsFinal(AnnotatedField exposed) {
        if (!Modifier.isFinal(exposed.getField().getModifiers())) {
            // FIXME We could make this a warning, but then the type compatibility check needs to happen at the moment
            // of injection, which may not be ideal (harder to see if there is something wrong in a real application)
            throw new DiscoveryException("{}, is not final");
        }
    }

    private void ensureIDIsNotAlreadyUsed(AnnotatedField annotated, Map<String, ExposedField> exposedByThisClass) {
        AnnotatedField alreadyExposed = idToExposedFields.get(annotated.getId());
        if (alreadyExposed == null) {
            alreadyExposed = exposedByThisClass.get(annotated.getId());
        }
        if (alreadyExposed != null) {
            throw new DiscoveryException(annotated.getLogString() +
                    ", cannot be exposed because the ID is already used to expose " +
                    alreadyExposed.getLogString());
        }
    }

    private void ensureExposedIsOfCompatibleType(ExposedField exposed) {
        if (exposed.getValue() == null) {
            throw new DiscoveryException(exposed.getLogString() + ", is null and therefore cannot be exposed");
        }
    }

    /**
     * Retrieves all methods exposed by the specified instance of the specified class.
     *
     * @param owner Class and instance owning the methods.
     * @return Mapping between IDs and exposed methods.
     */
    private Map<String, ExposedMethod> discoverExposedMethods(Owner owner) {
        Map<String, ExposedMethod> exposedByThisClass = new HashMap<>();

        if ((owner.getOwnerClass() != null) && !Object.class.equals(owner.getOwnerClass())) {
            // Get methods exposed in parent class
            exposedByThisClass.putAll(discoverExposedMethods(
                    new Owner(owner.getOwnerClass().getSuperclass(), owner.getOwnerInstance())));

            // Get methods exposed in this class
            for (Method method : owner.getOwnerClass().getDeclaredMethods()) {
                for (ExposeA e : method.getAnnotationsByType(ExposeA.class)) {
                    discoverExposedMethod(new AnnotatedMethodWithId(owner, method, e, e.value()), exposedByThisClass);
                }
            }
        }

        return exposedByThisClass;
    }

    private void discoverExposedMethod(AnnotatedMethodWithId annotated, Map<String, ExposedMethod> exposedByThisClass) {
        if ((annotated.getOwnerInstance() != null) || Modifier.isStatic(annotated.getMethod().getModifiers())) {
            // Some checks
            ensureIDIsNotAlreadyUsed(annotated, exposedByThisClass);

            ExposedMethod exposed = new ExposedMethod(annotated);
            exposedByThisClass.put(annotated.getId(), exposed);
        } else {
            LOGGER.warn("{}, will be ignored because it is not static", annotated.getLogString());
        }
    }

    private void ensureIDIsNotAlreadyUsed(AnnotatedMethodWithId annotated, Map<String, ExposedMethod> exposedByThisClass) {
        AnnotatedMethod alreadyExposed = idToExposedMethods.get(annotated.getId());
        if (alreadyExposed == null) {
            alreadyExposed = exposedByThisClass.get(annotated.getId());
        }
        if (alreadyExposed != null) {
            throw new DiscoveryException(annotated.getLogString() +
                    ", cannot be exposed because the ID is already used to expose " +
                    alreadyExposed.getLogString());
        }
    }

    /**
     * Retrieves all fields required by the specified instance of the specified class.
     *
     * @param owner Class and instance owning the fields.
     * @return Fields required by the specified instance.
     */
    private Collection<RequiredField> discoverRequiredFields(Owner owner) {
        Collection<RequiredField> required = new HashSet<>();

        if ((owner.getOwnerClass() != null) && !Object.class.equals(owner.getOwnerClass())) {
            // Get fields required by parent class
            required.addAll(discoverRequiredFields(
                    new Owner(owner.getOwnerClass().getSuperclass(), owner.getOwnerInstance())));

            // Get fields required by this class
            for (Field field : owner.getOwnerClass().getDeclaredFields()) {
                for (Require annotation : field.getAnnotationsByType(Require.class)) {
                    required.add(discoverRequiredField(
                            new AnnotatedField(owner, field, annotation, annotation.value()),
                            of(ReadableProperty.class,
                                    WritableProperty.class,
                                    ReadableWritableProperty.class,
                                    Runnable.class)
                                    .collect(toSet()))
                    );
                }
            }
        }

        return required;
    }

    private RequiredField discoverRequiredField(AnnotatedField annotated, Collection<Class<?>> possibleTypes) {
        // Some checks
        ensureFieldIsNotAlreadyAnnotated(annotated);
        ensureRequiredIsNotFinal(annotated);

        // Get value
        Object value;
        boolean wasAccessible = annotated.getField().isAccessible();
        annotated.getField().setAccessible(true);
        try {
            value = annotated.getField().get(annotated.getOwnerInstance());
        } catch (IllegalAccessException e) {
            // TODO Better message
            throw new DiscoveryException(e);
        } finally {
            annotated.getField().setAccessible(wasAccessible);
        }

        // More checks
        RequiredField required = new RequiredField(annotated, value);
        ensureRequiredIsOfCompatibleType(required, possibleTypes);

        return required;
    }

    private void ensureFieldIsNotAlreadyAnnotated(AnnotatedField required) {
        // TODO
    }

    private void ensureRequiredIsNotFinal(AnnotatedField exposed) {
        if (Modifier.isFinal(exposed.getField().getModifiers())) {
            throw new DiscoveryException(exposed.getLogString() +
                    ", is final and therefore cannot be injected");
        }
    }

    private void ensureRequiredIsOfCompatibleType(RequiredField required, Collection<Class<?>> possibleTypes) {
        Object value = required.getValue();
        if (value != null) {
            // TODO Log warning instead? (although it can be confusing, it can still work)
            throw new DiscoveryException(required.getLogString() +
                    ", already has value '" + required.getValue() +
                    "' and therefore cannot be injected");
        } else if (possibleTypes.stream().noneMatch(t -> required.getFieldClass().isAssignableFrom(t))) {
            throw new DiscoveryException(required.getLogString() +
                    ", is declared with type '" + required.getFieldClassName() +
                    "' which is incompatible with the annotation");
        }
    }

    private List<PostInjectMethod> discoverPostInjectMethods(Owner owner) {
        List<PostInjectMethod> postInject = new ArrayList<>();

        if ((owner.getOwnerClass() != null) && !Object.class.equals(owner.getOwnerClass())) {
            // Get post-inject methods from parent class first
            postInject.addAll(discoverPostInjectMethods(
                    new Owner(owner.getOwnerClass().getSuperclass(), owner.getOwnerInstance())));

            // Get post-inject methods from this class (may override the parents' methods)
            for (Method method : owner.getOwnerClass().getDeclaredMethods()) {
                for (PostInject annotation : method.getAnnotationsByType(PostInject.class)) {
                    // Remove previous method from parent class(es) if overridden in child
                    postInject.removeIf(e -> ReflectionUtils.overrides(e.getMethod(), method));
                    postInject.add(discoverPostInjectMethod(new AnnotatedMethod(owner, method, annotation)));
                }
            }
        }

        return postInject;
    }

    private PostInjectMethod discoverPostInjectMethod(AnnotatedMethod annotated) {
        // Some checks
        ensureMethodHasNoParameter(annotated);

        return new PostInjectMethod(annotated);
    }

    private void ensureMethodHasNoParameter(AnnotatedMethod exposed) {
        if (exposed.getMethod().getParameterCount() > 0) {
            throw new DiscoveryException(exposed.getLogString() + ", should have no parameter");
        }
    }

    /**
     * Tries to injected the remaining required fields based on the (newly) exposed fields.
     *
     * @return Collection of owner that are not fully injected.
     */
    private Collection<Owner> injectUnresolvedRequiredFields() {
        Collection<Owner> fullyResolvedOwners = new ArrayList<>();

        for (Map.Entry<Owner, Collection<RequiredField>> entry : ownerToUnresolvedRequiredFields.entrySet()) {
            Collection<RequiredField> unresolvedFields = entry.getValue();
            Collection<RequiredField> newlyResolvedFields = new HashSet<>();

            for (RequiredField required : unresolvedFields) {
                ExposedField exposed = idToExposedFields.get(required.getId());
                if (exposed == null) {
                    LOGGER.debug("{}, cannot yet be injected because no field is exposed with this ID",
                            required.getLogString());
                } else {
                    injectField(required, exposed);
                    newlyResolvedFields.add(required);
                }
            }

            // Remove required fields that have been injected from the pending list
            unresolvedFields.removeAll(newlyResolvedFields);

            if (unresolvedFields.isEmpty()) {
                fullyResolvedOwners.add(entry.getKey());
            }
        }

        // Remove owners that have been fully injected from the pending map
        fullyResolvedOwners.forEach(ownerToUnresolvedRequiredFields::remove);

        return fullyResolvedOwners;
    }

    private void injectField(RequiredField required, ExposedField exposed) {
        // We assume here that the value of the exposed field is not null and of a correct type

        ensureRequiredIsInjectable(required, exposed);

        Object wrappedValue;
        try {
            wrappedValue = getValueForRequiredField(required.getId(), required.getFieldClass());
        } catch (ClassCastException | IllegalArgumentException e) {
            // TODO Better message
            throw new InjectionException(e);
        }
        if (wrappedValue == null) {
            // Internal problem
            throw new InjectionException();
        } else {
            // Inject
            boolean wasAccessible = required.getField().isAccessible();
            required.getField().setAccessible(true);
            try {
                required.getField().set(required.getOwnerInstance(), wrappedValue);
            } catch (IllegalAccessException e) {
                // TODO Better message
                throw new InjectionException(e);
            } finally {
                required.getField().setAccessible(wasAccessible);
            }
        }
    }

    private void ensureRequiredIsInjectable(RequiredField required, ExposedField exposed) {
        Object currentValue;
        boolean wasAccessible = required.getField().isAccessible();
        required.getField().setAccessible(true);
        try {
            currentValue = required.getField().get(required.getOwnerInstance());
        } catch (IllegalAccessException e) {
            // TODO Better message
            throw new InjectionException(e);
        } finally {
            required.getField().setAccessible(wasAccessible);
        }

        if (currentValue == null) {
            // TODO Check type compatibility?
        } else {
            // TODO Make it a warning instead of an exception
            throw new InjectionException(required.getLogString() +
                    ", already has a non-null value of '" +
                    currentValue +
                    "' and therefore cannot be injected");
        }
    }

    public <T> T getValueForRequiredField(String requiredId, Class<T> requiredClass)
            throws ClassCastException, IllegalArgumentException {
        T value;

        ExposedField exposed = idToExposedFields.get(requiredId);
        if (exposed == null) {
            LOGGER.debug("No field is exposed with ID '{}' yet", requiredId);
            value = null;
        } else {
            value = converters.convert(exposed.getValue(), requiredClass);

            if (value == null) {
                // TODO Better message
                throw new InjectionException("No appropriate converter registered");
            }
        }

        return value;
    }

    private Object wrapValueAsReadOnly(ExposedField exposed, RequiredField required) {
        try {
            ReadableProperty value = (ReadableProperty) exposed.getValue();
            return new ReadOnlyPropertyWrapper(value);
        } catch (ClassCastException e) {
            throw new InjectionException(exposed.getLogString() +
                    ", cannot be injected into " + required.getLogString(), e);
        }
    }

    private Object wrapValueAsWriteOnly(ExposedField exposed, RequiredField required) {
        try {
            WritableProperty value = (WritableProperty) exposed.getValue();
            return new WriteOnlyPropertyWrapper(value);
        } catch (ClassCastException e) {
            throw new InjectionException(exposed.getLogString() +
                    ", cannot be injected into " + required.getLogString(), e);
        }
    }

    private Object wrapValueAsReadWrite(ExposedField exposed, RequiredField required) {
        try {
            ReadableWritableProperty value = (ReadableWritableProperty) exposed.getValue();
            return new ReadWritePropertyWrapper(value);
        } catch (ClassCastException e) {
            throw new InjectionException(exposed.getLogString() +
                    ", cannot be injected into " + required.getLogString(), e);
        }
    }

    private Object wrapValueAsRunnable(ExposedField exposed, RequiredField required) {
        try {
            Runnable value = (Runnable) exposed.getValue();
            return new RunnableWrapper(value);
        } catch (ClassCastException e) {
            throw new InjectionException(exposed.getLogString() +
                    ", cannot be injected into " + required.getLogString(), e);
        }
    }

    private void callPostInjectMethods(Collection<Owner> fullyInjectedOwners, Owner newlyRegisteredOwner) {
        // Call PostInject methods on previously registered owners
        fullyInjectedOwners.stream()
                .map(ownerToPostInjectMethods::get)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .forEach(this::callPostInjectMethod);

        // Call PostInject methods on newly registered owner if it does not require any
        if (!ownerToRequiredFields.containsKey(newlyRegisteredOwner)) {
            List<PostInjectMethod> methods = ownerToPostInjectMethods.get(newlyRegisteredOwner);
            if (methods != null) {
                methods.forEach(this::callPostInjectMethod);
            }
        }
    }

    private void callPostInjectMethod(PostInjectMethod postInject) {
        Method method = postInject.getMethod();

        boolean wasAccessible = method.isAccessible();
        method.setAccessible(true);
        try {
            method.invoke(postInject.getOwnerInstance());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new InjectionException(postInject.getLogString() +
                    ", cannot be called after class/instance has been fully resolved'", e);
        } finally {
            method.setAccessible(wasAccessible);
        }
    }
}
