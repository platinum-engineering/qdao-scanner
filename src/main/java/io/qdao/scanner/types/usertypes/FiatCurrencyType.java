package io.qdao.scanner.types.usertypes;

import io.qdao.scanner.types.FiatCurrency;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class FiatCurrencyType implements UserType {

    private final int[] arrayTypes = new int[]{Types.OTHER};
    private final static String typeName = "fiat_currency";

    @Override
    public int[] sqlTypes() {
        return arrayTypes;
    }

    @Override
    public Class<FiatCurrency> returnedClass() {
        return FiatCurrency.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return x == null ? y == null : x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        if (names != null && names.length > 0 && rs != null && rs.getArray(names[0]) != null) {
            final Object obj = rs.getObject(names[0]);
            if (obj instanceof String) {
                final String result = (String) obj;
                return FiatCurrency.valueOf(result);
            }
        }
        return null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (st == null) {
            return;
        }
        if (value != null) {
            final FiatCurrency os = (FiatCurrency) value;
            st.setObject(index, os.toString(), arrayTypes[0]);
        } else {
            st.setNull(index, arrayTypes[0]);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        if (value instanceof FiatCurrency) {
            final FiatCurrency os = (FiatCurrency) value;
            return FiatCurrency.valueOf(os.toString());
        }
        return null;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}
