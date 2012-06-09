package de.brands4friends.daleq.internal.builder;

import static de.brands4friends.daleq.Daleq.aRow;
import static de.brands4friends.daleq.Daleq.aTable;
import static de.brands4friends.daleq.internal.builder.ExampleTable.PROP_A;
import static de.brands4friends.daleq.internal.builder.ExampleTable.PROP_B;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.dbunit.dataset.datatype.DataType;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import de.brands4friends.daleq.DaleqBuildException;
import de.brands4friends.daleq.FieldDef;
import de.brands4friends.daleq.TableDef;
import de.brands4friends.daleq.internal.structure.TableStructure;
import de.brands4friends.daleq.internal.structure.TableStructureFactory;

public class TableBuilderTest {

    private Context context;
    private StructureBuilder sb;

    @Before
    public void setUp() throws Exception {
        context = new SimpleContext();
        final TableStructure tableStructure = new TableStructureFactory().create(ExampleTable.class);
        sb = new StructureBuilder(tableStructure);
    }

    @Test
    public void aTableWithARow_should_beBuilt() {
        assertThat(
                aTable(ExampleTable.class).with(aRow(42)).build(context),
                is(sb.table(
                        sb.row(
                                sb.field(PROP_A, "42"),
                                sb.field(PROP_B, "42")
                        )
                ))
        );
    }

    @Test
    public void aTableWithSomeExplicitAddedRow_should_beBuilt() {
        assertThat(
                aTable(ExampleTable.class).with(aRow(1), aRow(2), aRow(3)).build(context),
                is(
                        sb.table(
                                sb.row(sb.field(PROP_A, "1"), sb.field(PROP_B, "1")),
                                sb.row(sb.field(PROP_A, "2"), sb.field(PROP_B, "2")),
                                sb.row(sb.field(PROP_A, "3"), sb.field(PROP_B, "3"))
                        ))
        );
    }

    @Test
    public void aTableWithSomeRows_should_beBuilt() {
        assertThat(
                aTable(ExampleTable.class).withSomeRows(Lists.newArrayList(1l, 2l, 3l)).build(context),
                is(
                        sb.table(
                                sb.row(sb.field(PROP_A, "1"), sb.field(PROP_B, "1")),
                                sb.row(sb.field(PROP_A, "2"), sb.field(PROP_B, "2")),
                                sb.row(sb.field(PROP_A, "3"), sb.field(PROP_B, "3"))
                        ))
        );
    }

    @Test
    public void aTableWithSomeRowsEllipsis_should_beBuilt() {
        assertThat(
                aTable(ExampleTable.class).withSomeRows(1l, 2l, 3l).build(context),
                is(
                        sb.table(
                                sb.row(sb.field(PROP_A, "1"), sb.field(PROP_B, "1")),
                                sb.row(sb.field(PROP_A, "2"), sb.field(PROP_B, "2")),
                                sb.row(sb.field(PROP_A, "3"), sb.field(PROP_B, "3"))
                        ))
        );
    }

    @Test
    public void aTableWithRowsUntil_should_beBuilt(){
        assertThat(
                aTable(ExampleTable.class).withRowsUntil(4).build(context),
                is(
                        sb.table(
                                sb.row(sb.field(PROP_A, "0"), sb.field(PROP_B, "0")),
                                sb.row(sb.field(PROP_A, "1"), sb.field(PROP_B, "1")),
                                sb.row(sb.field(PROP_A, "2"), sb.field(PROP_B, "2")),
                                sb.row(sb.field(PROP_A, "3"), sb.field(PROP_B, "3"))
                        ))
        );
    }

    @TableDef("ANOTHER_TABLE")
    public static class AnotherTable {
        public static final FieldDef ANOTHER_FIELD = FieldDef.fd(DataType.INTEGER);
    }

    @Test(expected = DaleqBuildException.class)
    public void aTableWithFieldsFromAnotherTable_should_fail(){
        aTable(ExampleTable.class).with(aRow(1).f(AnotherTable.ANOTHER_FIELD,123)).build(context);
    }
}
