/*
 * Copyright 1999-2017 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.druid.sql.dialect.postgresql.ast.stmt;

import com.alibaba.druid.sql.JdbcConstants;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

import java.util.ArrayList;
import java.util.List;

public class PGInsertStatement extends SQLInsertStatement implements PGSQLStatement {


    private List<ValuesClause> valuesList = new ArrayList<ValuesClause>();
    private SQLExpr returning;
    private boolean			   defaultValues = false;

    public PGInsertStatement() {
        dbType = JdbcConstants.POSTGRESQL;
    }

    public void cloneTo(PGInsertStatement x) {
        super.cloneTo(x);
        for (ValuesClause v : valuesList) {
            ValuesClause v2 = v.clone();
            v2.setParent(x);
            x.valuesList.add(v2);
        }
        if (returning != null) {
            x.setReturning(returning.clone());
        }
        x.defaultValues = defaultValues;
    }

    public SQLExpr getReturning() {
        return returning;
    }

    public void setReturning(SQLExpr returning) {
        this.returning = returning;
    }


    public ValuesClause getValues() {
        if (valuesList.size() == 0) {
            return null;
        }
        return valuesList.get(0);
    }

    public void setValues(ValuesClause values) {
        if (valuesList.size() == 0) {
            valuesList.add(values);
        } else {
            valuesList.set(0, values);
        }
    }

    public List<ValuesClause> getValuesList() {
        return valuesList;
    }

    public void addValueCause(ValuesClause valueClause) {
        valueClause.setParent(this);
        valuesList.add(valueClause);
    }

    public boolean isDefaultValues() {
		return defaultValues;
	}

	public void setDefaultValues(boolean defaultValues) {
		this.defaultValues = defaultValues;
	}

	protected void accept0(SQLASTVisitor visitor) {
        accept0((PGASTVisitor) visitor);
    }

    @Override
    public void accept0(PGASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, with);
            this.acceptChild(visitor, tableSource);
            this.acceptChild(visitor, columns);
            this.acceptChild(visitor, valuesList);
            this.acceptChild(visitor, query);
            this.acceptChild(visitor, returning);
        }

        visitor.endVisit(this);
    }

    public PGInsertStatement clone() {
        PGInsertStatement x = new PGInsertStatement();
        cloneTo(x);
        return x;
    }
}
