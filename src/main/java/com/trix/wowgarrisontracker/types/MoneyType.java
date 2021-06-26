package com.trix.wowgarrisontracker.types;

import com.trix.wowgarrisontracker.pojos.Money;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

public class MoneyType implements UserType {

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.BIGINT};
    }

    @Override
    public Class<Money> returnedClass() {
        return Money.class;
    }

    @Override
    public boolean equals(Object o, Object o1) throws HibernateException {
        return o.equals(o1);
    }

    @Override
    public int hashCode(Object o) throws HibernateException {
        return o.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet,
                              String[] strings,
                              SharedSessionContractImplementor sharedSessionContractImplementor,
                              Object o) throws HibernateException, SQLException {
        Long copper = resultSet.getLong(strings[0]);

        if (resultSet.wasNull())
            return null;

        return new Money(copper);
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object o, int i, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {

        if(Objects.isNull(o)){
            preparedStatement.setNull(i, Types.BIGINT);
        }else{
            preparedStatement.setLong(i,((Money)o).getCopperValue());
        }

    }

    @Override
    public Object deepCopy(Object o) throws HibernateException {
        if(o == null)
            return new Money();
        return new Money(((Money)o).getCopperValue());
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object o) throws HibernateException {
        return (Serializable)o;
    }

    @Override
    public Object assemble(Serializable serializable, Object o) throws HibernateException {
        return o;
    }

    @Override
    public Object replace(Object o, Object o1, Object o2) throws HibernateException {
        return o;
    }
}
