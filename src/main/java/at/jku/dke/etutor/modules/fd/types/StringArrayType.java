package at.jku.dke.etutor.modules.fd.types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.*;
import java.util.Arrays;

public class StringArrayType <T extends Serializable> implements UserType {
//    protected static final int[] SQL_TYPES = {Types.ARRAY};
//    private Class<T> typeParameterClass;
//
//    @Override
//    public Object assemble(Serializable cached, Object owner) throws HibernateException {
//        return this.deepCopy(cached);
//    }
//
//    @Override
//    public Object deepCopy(Object value) throws HibernateException {
//        return value;
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public Serializable disassemble(Object value) throws HibernateException {
//        return (T) this.deepCopy(value);
//    }
//
//    @Override
//    public boolean equals(Object x, Object y) throws HibernateException {
//
//        if (x == null) {
//            return y == null;
//        }
//        return x.equals(y);
//    }
//
//    @Override
//    public int hashCode(Object x) throws HibernateException {
//        return x.hashCode();
//    }
//
////    @Override
////    public Object nullSafeGet(ResultSet resultSet, String[] names,
////                              SharedSessionContractImplementor sharedSessionContractImplementor,
////                              Object o) throws HibernateException, SQLException {
////        if (resultSet.wasNull()) {
////            return null;
////        }
////        if (resultSet.getArray(names[0]) == null) {
////            return new Integer[0];
////        }
////
////        Array array = resultSet.getArray(names[0]);
////        @SuppressWarnings("unchecked")
////        T javaArray = (T) array.getArray();
////        return javaArray;
////    }
//    @Override
//    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
//            throws HibernateException, SQLException {
//        Array array = rs.getArray(names[0]);
//        return array != null ? array.getArray() : null;
//    }
//
////    @Override
////    public void nullSafeSet(PreparedStatement statement, Object value, int index,
////                            SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
////        Connection connection = statement.getConnection();
////        if (value == null) {
////            statement.setNull(index, SQL_TYPES[0]);
////        } else {
////            @SuppressWarnings("unchecked")
////            T castObject = (T) value;
////            Array array = connection.createArrayOf("text", (Object[]) castObject);
////            statement.setArray(index, array);
////        }
////    }
//    @Override
//    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
//            throws HibernateException, SQLException {
//        if (value != null && st != null) {
//            Array array = session.connection().createArrayOf("text", (String[])value);
//            st.setArray(index, array);
//        } else {
//            st.setNull(index, sqlTypes()[0]);
//        }
//    }
//
//    @Override
//    public boolean isMutable() {
//        return true;
//    }
//
//    @Override
//    public Object replace(Object original, Object target, Object owner) throws HibernateException {
//        return original;
//    }
//
//    @Override
//    public Class<T> returnedClass() {
//        return typeParameterClass;
//    }
//
//    @Override
//    public int[] sqlTypes() {
//        return new int[]{Types.ARRAY};
//    }
@Override
public int[] sqlTypes() {
    return new int[]{Types.ARRAY};
}

    @Override
    public Class returnedClass() {
        return String[].class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x instanceof String[] && y instanceof String[]) {
            return Arrays.deepEquals((String[])x, (String[])y);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return Arrays.hashCode((String[])x);
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
            throws HibernateException, SQLException {
        Array array = rs.getArray(names[0]);
        return array != null ? array.getArray() : null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
            throws HibernateException, SQLException {
        if (value != null && st != null) {
            Array array = session.connection().createArrayOf("text", (String[])value);
            st.setArray(index, array);
        } else {
            st.setNull(index, sqlTypes()[0]);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        String[] a = (String[])value;
        return Arrays.copyOf(a, a.length);
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