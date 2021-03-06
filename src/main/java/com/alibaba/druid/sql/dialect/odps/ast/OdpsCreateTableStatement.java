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
package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.JdbcConstants;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

import java.util.ArrayList;
import java.util.List;

public class OdpsCreateTableStatement extends SQLCreateTableStatement {

    private SQLExprTableSource like;

    protected List<SQLColumnDefinition> partitionColumns = new ArrayList<SQLColumnDefinition>(2);

    protected final List<SQLName> clusteredBy = new ArrayList<SQLName>();
    protected final List<SQLName> sortedBy = new ArrayList<SQLName>();

    protected int buckets;

    protected SQLExpr lifecycle;

    public OdpsCreateTableStatement(){
        super(JdbcConstants.ODPS);
    }

    public SQLExprTableSource getLike() {
        return like;
    }

    public void setLike(SQLName like) {
        this.setLike(new SQLExprTableSource(like));
    }

    public void setLike(SQLExprTableSource like) {
        this.like = like;
    }

    public List<SQLColumnDefinition> getPartitionColumns() {
        return partitionColumns;
    }
    
    public void addPartitionColumn(SQLColumnDefinition column) {
        if (column != null) {
            column.setParent(this);
        }
        this.partitionColumns.add(column);
    }

    public SQLExpr getLifecycle() {
        return lifecycle;
    }

    public void setLifecycle(SQLExpr lifecycle) {
        this.lifecycle = lifecycle;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        accept0((OdpsASTVisitor) visitor);
    }

    protected void accept0(OdpsASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, tableSource);
            this.acceptChild(visitor, tableElementList);
            this.acceptChild(visitor, partitionColumns);
            this.acceptChild(visitor, clusteredBy);
            this.acceptChild(visitor, sortedBy);
            this.acceptChild(visitor, lifecycle);
            this.acceptChild(visitor, select);
        }
        visitor.endVisit(this);
    }

    public List<SQLName> getClusteredBy() {
        return clusteredBy;
    }

    public List<SQLName> getSortedBy() {
        return sortedBy;
    }

    public int getBuckets() {
        return buckets;
    }

    public void setBuckets(int buckets) {
        this.buckets = buckets;
    }
}
