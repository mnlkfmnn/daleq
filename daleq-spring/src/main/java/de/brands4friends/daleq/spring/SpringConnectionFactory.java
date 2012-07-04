/*
 * Copyright 2012 brands4friends, Private Sale GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.brands4friends.daleq.spring;

import java.sql.Connection;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.google.common.base.Preconditions;

import de.brands4friends.daleq.core.DaleqException;
import de.brands4friends.daleq.core.internal.dbunit.ConnectionFactory;

public class SpringConnectionFactory implements ConnectionFactory {

    private DataSource dataSource;

    private IDataTypeFactory dataTypeFactory = new HsqldbDataTypeFactory();

    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDataTypeFactory(final IDataTypeFactory dataTypeFactory) {
        this.dataTypeFactory = dataTypeFactory;
    }

    @Override
    @SuppressWarnings("PMD.CloseResource") // this is a factory. it will not close the resource for sure!
    public IDatabaseConnection createConnection() {
        Preconditions.checkNotNull(dataSource, "dataSource is null.");
        try {
            final Connection conn = DataSourceUtils.getConnection(dataSource);
            final DatabaseConnection databaseConnection = new DatabaseConnection(conn);
            databaseConnection.getConfig().setProperty(
                    "http://www.dbunit.org/properties/datatypeFactory", dataTypeFactory);
            return databaseConnection;
        } catch (DatabaseUnitException e) {
            throw new DaleqException(e);
        }
    }
}