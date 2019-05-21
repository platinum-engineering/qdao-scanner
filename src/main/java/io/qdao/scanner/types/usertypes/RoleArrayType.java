package io.qdao.scanner.types.usertypes;

import io.qdao.scanner.types.Role;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleArrayType implements UserType {

    private final int[] arrayTypes = new int[] { Types.ARRAY };
    private final static String typeName = "app_roles";
    @Override
    public int[] sqlTypes() {
        return arrayTypes;
    }

    @Override
    public Class<List> returnedClass() {
        return List.class;
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
            final String[] results = (String[]) rs.getArray(names[0]).getArray();
            final ArrayList<Role> roles = new ArrayList<>();
            for (String res : results) {
                final Role r = Role.valueOf(res);
                roles.add(r);
            }
            return roles;
        }
        return new ArrayList<Role>();
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value != null && st != null) {
            final List<Role> list = (List<Role>) value;
            final ArrayList<String> strs = new ArrayList<>();
            for (Role ar : list) {
                strs.add(ar.toString());
            }
            final String[] castObject = strs.toArray(new String[list.size()]);
            final Array array = session.connection().createArrayOf(typeName, castObject);
            st.setArray(index, array);
        } else {
            st.setNull(index, arrayTypes[0]);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        if (value == null) {
            return null;
        }
        final List<Role> list = (List<Role>) value;
        final ArrayList<Role> clone = new ArrayList<Role>();
        clone.addAll(list);
        return clone;
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
