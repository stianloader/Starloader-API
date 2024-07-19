package de.geolykt.starloader.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Predicate;

import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.stianloader.micromixin.transform.internal.util.Objects;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.event.lifecycle.ApplicationStartedEvent;
import de.geolykt.starloader.api.gui.modconf.BooleanOption;
import de.geolykt.starloader.api.gui.modconf.ConfigurationOption;
import de.geolykt.starloader.api.gui.modconf.ConfigurationSection;
import de.geolykt.starloader.api.gui.modconf.FloatOption;
import de.geolykt.starloader.api.gui.modconf.IntegerOption;
import de.geolykt.starloader.api.gui.modconf.ModConf.ModConfSpec;
import de.geolykt.starloader.api.gui.modconf.StrictStringOption;
import de.geolykt.starloader.api.gui.modconf.StringChooseOption;
import de.geolykt.starloader.api.gui.modconf.StringOption;
import de.geolykt.starloader.api.utils.FloatConsumer;
import de.geolykt.starloader.impl.gui.ModConfScreen;
import de.geolykt.starloader.impl.util.PseudoImmutableArrayList;

/**
 * Default implementation of the ModConf classes that is used within this
 * Starloader API Implementation.
 *
 * @since 1.3.0
 */
public class ModConf implements ModConfSpec {

    /**
     * Class that unifies value-independent methods so they are not implemented
     * again a few hundred times.
     *
     * @param <T> The data value used by {@link ConfigurationOption}.
     * @since 1.3.0
     */
    public abstract static class SLBaseOption<T> implements ConfigurationOption<T> {

        /**
         * The section that this option currently resides in.
         *
         * @since 1.3.0
         */
        @NotNull
        protected final ConfigurationSection cfgSect;

        /**
         * The name of the option. Should be user-friendly but not all too long.
         *
         * @since 1.3.0
         */
        @NotNull
        protected final String name;

        /**
         * Constructor.
         *
         * @param name    The user-friendly name of this option. See {@link #getName()}
         *                for more info
         * @param cfgSect The parent section used by {@link #getParent()}
         * @since 1.3.0
         */
        protected SLBaseOption(@NotNull String name, @NotNull ConfigurationSection cfgSect) {
            this.name = name;
            this.cfgSect = cfgSect;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull
        public String getName() {
            return this.name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull
        public ConfigurationSection getParent() {
            return this.cfgSect;
        }
    }

    /**
     * Simplistic implementation of the {@link BooleanOption} interface.
     *
     * @since 1.3.0
     */
    public static class SLBooleanOption extends SLObjectOption<Boolean> implements BooleanOption {

        /**
         * The default value. What is considered default is more or less arbitrary, but
         * having one is required.
         *
         * @since 2.0.0-a20240719
         */
        private boolean currentVal;

        /**
         * The currently valid value.
         *
         * @since 2.0.0-a20240719
         */
        private final boolean defaultVal;

        @AvailableSince("2.0.0-a20240719")
        protected SLBooleanOption(@NotNull String name, @NotNull ConfigurationSection cfgSect,
                boolean currentVal, boolean defaultVal) {
            super(name, cfgSect);
            this.currentVal = currentVal;
            this.defaultVal = defaultVal;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull
        public Boolean get() {
            return this.currentVal;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull
        public Boolean getDefault() {
            return this.defaultVal;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void set(@NotNull Boolean value) {
            this.onSet(value);
            this.currentVal = value;
        }
    }

    /**
     * The implementation of the {@link ConfigurationSection} interface which is
     * used by this class.
     *
     * @since 1.3.0
     */
    public static class SLConfigSection implements ConfigurationSection {

        /**
         * The child options of this section.
         *
         * @since 1.3.0
         */
        @NotNull
        protected final PseudoImmutableArrayList<@NotNull ConfigurationOption<?>> children = new PseudoImmutableArrayList<>(16);

        /**
         * The user-friendly name of the section.
         *
         * @since 1.3.0
         */
        @NotNull
        protected final String name;

        /**
         * Constructor.
         *
         * @param name The user-friendly name of the section
         * @since 1.3.0
         */
        public SLConfigSection(@NotNull String name) {
            this.name = name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull
        public BooleanOption addBooleanOption(@NotNull String name, boolean currentValue, boolean defaultValue) {
            ModConf.checkState();
            BooleanOption opt = new SLBooleanOption(name, this, currentValue, defaultValue);
            this.children.unsafeAdd(opt);
            return opt;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull
        public FloatOption addFloatOption(@NotNull String name, float currentValue, float defaultValue,
                float min, float max, @NotNull Collection<@NotNull Float> recommended) {
            ModConf.checkState();
            FloatOption opt = new SLFLoatOption(name, this, currentValue, defaultValue, min, max, recommended);
            this.children.unsafeAdd(opt);
            return opt;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull
        public IntegerOption addIntegerOption(@NotNull String name, int currentValue, int defaultValue,
                int min, int max, @NotNull Collection<@NotNull Integer> recommended) {
            ModConf.checkState();
            IntegerOption opt = new SLIntOption(name, this, currentValue, defaultValue, min, max, recommended);
            this.children.unsafeAdd(opt);
            return opt;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void addOption(@NotNull ConfigurationOption<?> option) {
            ModConf.checkState();
            this.children.unsafeAdd(option);
        }

        @Override
        @NotNull
        public StringChooseOption addStringChooseOption(@NotNull String name,
                @NotNull String currentValue, @NotNull String defaultValue,
                @NotNull String @NotNull... options) {
            ModConf.checkState();
            StringChooseOption opt = new SLStringChooseOption(name, this, currentValue, defaultValue, Objects.requireNonNull(Arrays.asList(options)));
            this.children.unsafeAdd(opt);
            return opt;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull
        public StringOption addStringOption(@NotNull String name, @NotNull String currentValue,
                @NotNull String defaultValue, @NotNull Collection<@NotNull String> recommended) {
            ModConf.checkState();
            StringOption opt = new SLStringOption(name, this, currentValue, defaultValue, recommended);
            this.children.unsafeAdd(opt);
            return opt;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull
        public StrictStringOption addStringOption(@NotNull String name, @NotNull String currentValue,
                @NotNull String defaultValue, @NotNull Predicate<@NotNull String> test,
                @NotNull Collection<@NotNull String> recommended) {
            ModConf.checkState();
            StrictStringOption opt = new SLStrictStringOption(name, this, currentValue, defaultValue, test, recommended);
            this.children.unsafeAdd(opt);
            return opt;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull
        public List<@NotNull ConfigurationOption<?>> getChildren() {
            return this.children;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull
        public String getName() {
            return this.name;
        }
    }

    /**
     * Simplistic implementation of the {@link FloatOption} interface.
     *
     * @since 1.3.0
     */
    public static class SLFLoatOption extends SLBaseOption<Float> implements FloatOption {

        /**
         * The default value. What is considered default is more or less arbitrary, but
         * having one is required.
         *
         * @since 1.3.0
         */
        protected float defaultVal;

        /**
         * The listeners which have until now been registered via {@link #addValueChangeListener(FloatConsumer)}.
         *
         * @since 2.0.0
         */
        @NotNull
        protected final Collection<@NotNull FloatConsumer> listeners = new CopyOnWriteArrayList<>();

        protected float max;
        protected float min;

        /**
         * The recommended values shown to the user when the user decides to change the value.
         *
         * @since 1.3.0
         */
        protected @NotNull Collection<@NotNull Float> recommended;

        /**
         * The currently valid value.
         *
         * @since 1.3.0
         */
        protected float value;

        protected SLFLoatOption(@NotNull String name, @NotNull ConfigurationSection cfgSect, float current,
                float defaultVal, float min, float max, @NotNull Collection<@NotNull Float> recommended) {
            super(name, cfgSect);
            this.value = current;
            this.defaultVal = defaultVal;
            this.min = min;
            this.max = max;
            this.recommended = recommended;
        }

        @Override
        public void addValueChangeListener(@NotNull FloatConsumer listener) {
            this.listeners.add(listener);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull Float getDefault() {
            return defaultVal;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull Float getMaximum() {
            return max;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull Float getMinimum() {
            return min;
        }

        @Override
        public @NotNull Collection<@NotNull Float> getRecommendedValues() {
            return recommended;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public float getValue() {
            return value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setValue(float value) {
            if (value > max || value < min) {
                throw new IllegalArgumentException(
                        String.format(Locale.ROOT, "Value out of bounds: %f (min %f, max %f)", value, min, max));
            }
            for (FloatConsumer consumer : this.listeners) {
                consumer.accept(value);
            }
            this.value = value;
        }
    }

    /**
     * Simplistic implementation of the {@link IntegerOption} interface.
     */
    public static class SLIntOption extends SLBaseOption<Integer> implements IntegerOption {

        /**
         * The default value. What is considered default is more or less arbitrary, but
         * having one is required.
         *
         * @since 1.3.0
         */
        protected int defaultVal;

        /**
         * The listeners which have until now been registered via {@link #addValueChangeListener(IntConsumer)}.
         *
         * @since 2.0.0
         */
        @NotNull
        protected final Collection<@NotNull IntConsumer> listeners = new CopyOnWriteArrayList<>();

        protected int max;
        protected int min;

        /**
         * The recommended values shown to the user when the user decides to change the value.
         *
         * @since 1.3.0
         */
        protected @NotNull Collection<@NotNull Integer> recommended;

        /**
         * The currently valid value.
         *
         * @since 1.3.0
         */
        protected int value;

        protected SLIntOption(@NotNull String name, @NotNull ConfigurationSection cfgSect, int current, int defaultVal,
                int min, int max, @NotNull Collection<@NotNull Integer> recommended) {
            super(name, cfgSect);
            this.value = current;
            this.defaultVal = defaultVal;
            this.min = min;
            this.max = max;
            this.recommended = recommended;
        }

        @Override
        public void addValueChangeListener(@NotNull IntConsumer listener) {
            this.listeners.add(listener);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull Integer getDefault() {
            return defaultVal;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull Integer getMaximum() {
            return max;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull Integer getMinimum() {
            return min;
        }

        @Override
        public @NotNull Collection<@NotNull Integer> getRecommendedValues() {
            return recommended;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getValue() {
            return value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setValue(int value) {
            if (value > max || value < min) {
                throw new IllegalArgumentException(
                        String.format(Locale.ROOT, "Value out of bounds: %d (min %d, max %d)", value, min, max));
            }
            for (IntConsumer consumer : this.listeners) {
                consumer.accept(value);
            }
            this.value = value;
        }
    }

    /**
     * Class that unifies value-independent methods so they are not implemented
     * again a few hundred times.
     *
     * <p>However, unlike {@link SLBaseOption} this also adds listener methods for the
     * <b>generic</b> consumers. As not all subclasses of {@link SLBaseOption} (such as {@link SLIntOption})
     * use generic consumers as listeners, this class exists.
     *
     * @param <T> The data value used by {@link ConfigurationOption}.
     * @since 2.0.0
     */
    public abstract static class SLObjectOption<T> extends SLBaseOption<T> {

        /**
         * Constructor.
         *
         * @param name    The user-friendly name of this option. See {@link #getName()}
         *                for more info
         * @param cfgSect The parent section used by {@link #getParent()}
         * @since 2.0.0
         */
        protected SLObjectOption(@NotNull String name, @NotNull ConfigurationSection cfgSect) {
            super(name, cfgSect);
        }

        /**
         * The listeners which have until now been registered via {@link #addValueChangeListener(Consumer)}.
         *
         * @since 2.0.0
         */
        @NotNull
        protected final Collection<@NotNull Consumer<T>> listeners = new CopyOnWriteArrayList<>();

        @Override
        public void addValueChangeListener(@NotNull Consumer<T> listener) {
            this.listeners.add(listener);
        }

        protected void onSet(@NotNull T newValue) {
            for (Consumer<T> consumer : this.listeners) {
                consumer.accept(newValue);
            }
        }
    }

    /**
     * Simplistic implementation of the {@link StrictStringOption} interface. As the
     * interface states, this implementation verifies the validity of the inserted
     * values. The {@link StrictStringOption#isValid(String)} operation is delegated
     * to a {@link Function}.
     *
     * @since 1.3.0
     */
    public static class SLStrictStringOption extends SLStringOption implements StrictStringOption {

        /**
         * The function that is used to compute {@link #isValid(String)}.
         *
         * @since 1.3.0
         */
        protected final Predicate<@NotNull String> valitityTest;

        protected SLStrictStringOption(@NotNull String name, @NotNull ConfigurationSection cfgSect,
                @NotNull String currentVal, @NotNull String defaultVal,
                @NotNull Predicate<@NotNull String> valitityTest, @NotNull Collection<@NotNull String> recommended) {
            super(name, cfgSect, currentVal, defaultVal, recommended);
            this.valitityTest = valitityTest;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isValid(String value) {
            return value != null && valitityTest.test(value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void set(@NotNull String value) {
            if (!isValid(value)) {
                throw new IllegalStateException("Proposed value string is not valid.");
            }
            super.set(value);
        }
    }

    /**
     * Simplistic implementation of the {@link StringChooseOption} interface.
     * Instances can be created using {@link ConfigurationSection#addStringChooseOption(String, String, String, String...)}.
     *
     * @since 2.0.0-a20240719
     */
    @AvailableSince("2.0.0-a20240719")
    private static class SLStringChooseOption extends SLStrictStringOption implements StringChooseOption {
        @AvailableSince("2.0.0-a20240719")
        private SLStringChooseOption(@NotNull String name, @NotNull ConfigurationSection cfgSect,
                @NotNull String currentVal, @NotNull String defaultVal,
                @NotNull Collection<@NotNull String> supportedValues) {
            super(name, cfgSect, currentVal, defaultVal, supportedValues::contains, supportedValues);
        }
    }

    /**
     * Simplistic implementation of the {@link StringOption} interface. As the
     * interface states, this implementation does not verify strings for validity,
     * however sub-classes may do that.
     *
     * @since 1.3.0
     */
    public static class SLStringOption extends SLObjectOption<String> implements StringOption {

        /**
         * The default value. What is considered default is more or less arbitrary, but
         * having one is required.
         *
         * @since 2.0.0-a20240719
         */
        @NotNull
        private String currentVal;

        /**
         * The currently valid value.
         *
         * @since 2.0.0-a20240719
         */
        @NotNull
        private final String defaultVal;

        /**
         * The recommended values shown to the user when the user decides to change the value.
         *
         * @since 2.0.0-a20240719
         */
        @NotNull
        private Collection<@NotNull String> recommended;

        protected SLStringOption(@NotNull String name, @NotNull ConfigurationSection cfgSect,
                @NotNull String currentVal, @NotNull String defaultVal, @NotNull Collection<@NotNull String> recommended) {
            super(name, cfgSect);
            this.currentVal = currentVal;
            this.defaultVal = defaultVal;
            this.recommended = recommended;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull
        public String get() {
            return this.currentVal;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull
        public String getDefault() {
            return this.defaultVal;
        }

        @Override
        @NotNull
        public Collection<@NotNull String> getRecommendedValues() {
            return this.recommended;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void set(@NotNull String value) {
            this.onSet(value);
            this.currentVal = value;
        }
    }

    /**
     * Utility method that throws an exception if the application has already started.
     * Does not do anything else outside of that.
     *
     * @since 1.3.0
     */
    protected static final void checkState() {
        if (ApplicationStartedEvent.hasStarted()) {
            throw new IllegalStateException("Application has already started! Adding Sections or individual options not allowed.");
        }
    }

    @NotNull
    protected final ModConfScreen screen = new ModConfScreen(this);

    /**
     * The name of the currently registered sections. Used for easy state
     * validation.
     *
     * @since 1.3.0
     */
    protected final HashSet<@NotNull String> sectionNames = new HashSet<>();

    /**
     * The currently registered sections.
     *
     * @since 1.3.0
     */
    protected final @NotNull PseudoImmutableArrayList<@NotNull ConfigurationSection> sections = new PseudoImmutableArrayList<>(16);

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ConfigurationSection createSection(@NotNull String name) {
        if (!sectionNames.add(name)) {
            throw new IllegalStateException("A section was already registered with the same name.");
        }
        checkState();
        SLConfigSection sect = new SLConfigSection(name);
        sections.unsafeAdd(sect);
        return sect;
    }

    public @NotNull ModConfScreen getScreen() {
        return screen;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Collection<@NotNull ConfigurationSection> getSections() {
        return sections;
    }
}
