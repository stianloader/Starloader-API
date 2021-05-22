package de.geolykt.starloader.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.modconf.BooleanOption;
import de.geolykt.starloader.api.gui.modconf.ConfigurationOption;
import de.geolykt.starloader.api.gui.modconf.ConfigurationSection;
import de.geolykt.starloader.api.gui.modconf.FloatOption;
import de.geolykt.starloader.api.gui.modconf.IntegerOption;
import de.geolykt.starloader.api.gui.modconf.ModConf.ModConfSpec;
import de.geolykt.starloader.api.gui.modconf.StrictStringOption;
import de.geolykt.starloader.api.gui.modconf.StringOption;
import de.geolykt.starloader.impl.util.PseudoImmutableArrayList;

/**
 * Default implementation of the ModConf classes that is used within this
 * Starloader API Implementation.
 */
public class ModConf implements ModConfSpec {

    /**
     * Class that unifies value-independent methods so they are not implemented
     * again a few hundred times.
     *
     * @param <T> The data value used by {@link ConfigurationOption}.
     */
    public abstract static class SLBaseOption<T> implements ConfigurationOption<T> {

        protected final ConfigurationSection cfgSect;
        protected final String name;

        /**
         * Constructor.
         *
         * @param name    The user-friendly name of this option. See {@link #getName()}
         *                for more info
         * @param cfgSect The parent section used by {@link #getParent()}
         */
        protected SLBaseOption(@NotNull String name, @NotNull ConfigurationSection cfgSect) {
            this.name = name;
            this.cfgSect = cfgSect;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getName() {
            return name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull ConfigurationSection getParent() {
            return cfgSect;
        }
    }

    /**
     * Simplistic implementation of the {@link BooleanOption} interface.
     */
    public static class SLBooleanOption extends SLBaseOption<Boolean> implements BooleanOption {

        /**
         * The default value. What is considered default is more or less arbitrary, but
         * having one is required.
         */
        protected Boolean currentVal;

        /**
         * The currently valid value.
         */
        protected final Boolean defaultVal;

        protected SLBooleanOption(@NotNull String name, @NotNull ConfigurationSection cfgSect,
                @NotNull Boolean currentVal, @NotNull Boolean defaultVal) {
            super(name, cfgSect);
            this.currentVal = currentVal;
            this.defaultVal = defaultVal;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull Boolean get() {
            return currentVal;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull Boolean getDefault() {
            return defaultVal;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void set(@NotNull Boolean value) {
            currentVal = value;
        }
    }

    /**
     * The implementation of the {@link ConfigurationSection} interface which is
     * used by this class.
     */
    public static class SLConfigSection implements ConfigurationSection {

        /**
         * The child options of this section.
         */
        protected final PseudoImmutableArrayList<ConfigurationOption<?>> children = new PseudoImmutableArrayList<>(16);

        /**
         * The user-friendly name of the section.
         */
        protected final String name;

        /**
         * Constructor.
         *
         * @param name The user-friendly name of the section
         */
        public SLConfigSection(@NotNull String name) {
            this.name = name;
        }

        @Override
        public @NotNull BooleanOption addBooleanOption(@NotNull String name, boolean currentValue, boolean defaultValue) {
            BooleanOption opt = new SLBooleanOption(name, this, currentValue, defaultValue);
            children.unsafeAdd(opt);
            return opt;
        }

        @Override
        public @NotNull FloatOption addFloatOption(@NotNull String name, float currentValue, float defaultValue,
                float min, float max) {
            FloatOption opt = new SLFLoatOption(name, this, currentValue, defaultValue, min, max);
            children.unsafeAdd(opt);
            return opt;
        }

        @Override
        public @NotNull IntegerOption addIntegerOption(@NotNull String name, int currentValue, int defaultValue,
                int min, int max) {
            IntegerOption opt = new SLIntOption(name, this, currentValue, defaultValue, min, max);
            children.unsafeAdd(opt);
            return opt;
        }

        @Override
        public @NotNull StringOption addStringOption(@NotNull String name, @NotNull String currentValue,
                @NotNull String defaultValue) {
            StringOption opt = new SLStringOption(name, this, currentValue, defaultValue);
            children.unsafeAdd(opt);
            return opt;
        }

        @Override
        public @NotNull StrictStringOption addStringOption(@NotNull String name, @NotNull String currentValue,
                @NotNull String defaultValue, @NotNull Function<String, Boolean> test) {
            StrictStringOption opt = new SLStrictStringOption(name, this, currentValue, defaultValue, test);
            children.unsafeAdd(opt);
            return opt;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull List<@NotNull ConfigurationOption<?>> getChildren() {
            return children;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getName() {
            return name;
        }
    }

    /**
     * Simplistic implementation of the {@link FloatOption} interface.
     */
    public static class SLFLoatOption extends SLBaseOption<Float> implements FloatOption {

        /**
         * The default value. What is considered default is more or less arbitrary, but
         * having one is required.
         */
        protected float defaultVal;

        protected float max;
        protected float min;
        /**
         * The currently valid value.
         */
        protected float value;

        protected SLFLoatOption(@NotNull String name, @NotNull ConfigurationSection cfgSect, float current,
                float defaultVal, float min, float max) {
            super(name, cfgSect);
            this.value = current;
            this.defaultVal = defaultVal;
            this.min = min;
            this.max = max;
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
         */
        protected int defaultVal;

        protected int max;
        protected int min;
        /**
         * The currently valid value.
         */
        protected int value;

        protected SLIntOption(@NotNull String name, @NotNull ConfigurationSection cfgSect, int current, int defaultVal,
                int min, int max) {
            super(name, cfgSect);
            this.value = current;
            this.defaultVal = defaultVal;
            this.min = min;
            this.max = max;
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
            this.value = value;
        }
    }

    /**
     * Simplistic implementation of the {@link StrictStringOption} interface. As the
     * interface states, this implementation verifies the validity of the inserted
     * values. The {@link StrictStringOption#isValid(String)} operation is delegated
     * to a {@link Function}.
     */
    public static class SLStrictStringOption extends SLStringOption implements StrictStringOption {

        /**
         * The function that is used to compute {@link #isValid(String)}.
         */
        protected final Function<String, Boolean> valitityTest;

        protected SLStrictStringOption(@NotNull String name, @NotNull ConfigurationSection cfgSect,
                @NotNull String currentVal, @NotNull String defaultVal,
                @NotNull Function<String, Boolean> valitityTest) {
            super(name, cfgSect, currentVal, defaultVal);
            this.valitityTest = valitityTest;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isValid(String value) {
            return valitityTest.apply(value);
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
     * Simplistic implementation of the {@link StringOption} interface. As the
     * interface states, this implementation does not verify strings for validity,
     * however sub-classes may do that.
     */
    public static class SLStringOption extends SLBaseOption<String> implements StringOption {

        /**
         * The default value. What is considered default is more or less arbitrary, but
         * having one is required.
         */
        protected String currentVal;

        /**
         * The currently valid value.
         */
        protected final String defaultVal;

        protected SLStringOption(@NotNull String name, @NotNull ConfigurationSection cfgSect,
                @NotNull String currentVal, @NotNull String defaultVal) {
            super(name, cfgSect);
            this.currentVal = currentVal;
            this.defaultVal = defaultVal;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String get() {
            return currentVal;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getDefault() {
            return defaultVal;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void set(@NotNull String value) {
            currentVal = value;
        }
    }

    /**
     * The name of the currently registered sections. Used for easy state
     * validation.
     */
    protected final HashSet<@NotNull String> sectionNames = new HashSet<>();

    /**
     * The currently registered sections.
     */
    protected final PseudoImmutableArrayList<ConfigurationSection> sections = new PseudoImmutableArrayList<>(16);

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ConfigurationSection createSection(@NotNull String name) {
        if (!sectionNames.add(name)) {
            throw new IllegalStateException("A section was already registered with the same name.");
        }
        SLConfigSection sect = new SLConfigSection(name);
        sections.unsafeAdd(sect);
        return sect;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Collection<@NotNull ConfigurationSection> getSections() {
        return sections;
    }
}
