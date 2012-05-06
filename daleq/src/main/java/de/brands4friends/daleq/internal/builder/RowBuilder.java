package de.brands4friends.daleq.internal.builder;

import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.brands4friends.daleq.DaleqBuildException;
import de.brands4friends.daleq.PropertyDef;
import de.brands4friends.daleq.Row;
import de.brands4friends.daleq.internal.container.PropertyContainer;
import de.brands4friends.daleq.internal.container.RowContainer;
import de.brands4friends.daleq.internal.structure.PropertyStructure;
import de.brands4friends.daleq.internal.structure.TableStructure;
import de.brands4friends.daleq.internal.structure.TemplateValue;

public class RowBuilder implements Row {

    private final Object binding;
    private final List<PropertyHolder> properties;

    public RowBuilder(final Object binding) {
        this.binding = binding;
        this.properties = Lists.newArrayList();
    }

    public Row p(PropertyDef propertyDef, Object value) {
        properties.add(new PropertyHolder(propertyDef, value));
        return this;
    }

    @Override
    public RowContainer build(final Context context, final TableStructure tableStructure) {
        final Map<PropertyStructure, PropertyHolder> structureToHolder = createStructureToHolderIndex(tableStructure);
        final List<PropertyContainer> propertyContainers = mapPropertiesToContainers(context, tableStructure, structureToHolder);
        return new RowContainer(tableStructure, propertyContainers);
    }

    private List<PropertyContainer> mapPropertiesToContainers(
            final Context context,
            final TableStructure tableStructure,
            final Map<PropertyStructure, PropertyHolder> structureToHolder) {

        return Lists.transform(tableStructure.getProperties(), new Function<PropertyStructure, PropertyContainer>() {
            @Override
            public PropertyContainer apply(final PropertyStructure propertyStructure) {
                final PropertyHolder actualProperty = structureToHolder.get(propertyStructure);
                if (actualProperty != null) {
                    return convertProvidedProperty(propertyStructure, actualProperty, context);
                } else {
                    return convertDefaultProperty(propertyStructure, context);
                }
            }
        });
    }

    private PropertyContainer convertDefaultProperty(final PropertyStructure propertyStructure, final Context context) {
        // apply template binding to template
        final String coercedBinding = convert(context,binding);
        final TemplateValue templateValue = propertyStructure.getTemplateValue();
        final String renderedValue = templateValue.render(coercedBinding);
        return new PropertyContainer(propertyStructure,renderedValue);
    }

    private PropertyContainer convertProvidedProperty(final PropertyStructure propertyStructure, final PropertyHolder actualProperty, final Context context) {
        final String strValue = convert(context, actualProperty.getValue());
        return new PropertyContainer(propertyStructure, strValue);
    }

    private String convert(final Context context, final Object valueToConvert) {
        return context.getTypeConversion().convert(valueToConvert);
    }

    private Map<PropertyStructure, PropertyHolder> createStructureToHolderIndex(final TableStructure tableStructure) {
        return Maps.uniqueIndex(properties, new Function<PropertyHolder, PropertyStructure>() {
            @Override
            public PropertyStructure apply(final PropertyHolder propertyHolder) {
                final PropertyStructure propertyStructure = tableStructure.findStructureByDef(propertyHolder.getPropertyDef());
                if (propertyStructure == null) {
                    final String msg = String.format(
                            "The row contains a property '%s', but the table '%s' does not contain such a property definition.",
                            propertyHolder.getPropertyDef(),
                            tableStructure.getName()
                    );
                    throw new DaleqBuildException(msg);
                }
                return propertyStructure;
            }
        });
    }

    public static RowBuilder row(final Object binding) {
        return new RowBuilder(binding);
    }
}