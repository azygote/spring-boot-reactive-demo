package org.gty.demo.util;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.PGConnection;
import org.postgresql.largeobject.LargeObjectManager;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.Objects;

public class PostgresOidTypeHandler extends BaseTypeHandler<byte[]> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, byte[] parameter, JdbcType jdbcType) throws SQLException {
        var conn = ps.getConnection();

        var lobj = conn.unwrap(PGConnection.class).getLargeObjectAPI();

        var oid = lobj.createLO(LargeObjectManager.READ | LargeObjectManager.WRITE);

        try (var obj = lobj.open(oid, LargeObjectManager.WRITE)) {
            obj.write(parameter);
        }

        ps.setLong(i, oid);
    }

    @Override
    public byte[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        var conn = rs.getStatement().getConnection();
        var oid = rs.getLong(columnName);

        return getNullableResult(conn, oid);
    }

    @Override
    public byte[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        var conn = rs.getStatement().getConnection();
        var oid = rs.getLong(columnIndex);

        return getNullableResult(conn, oid);
    }

    @Override
    public byte[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        var conn = cs.getConnection();
        var oid = cs.getLong(columnIndex);

        return getNullableResult(conn, oid);
    }

    private byte[] getNullableResult(@Nonnull Connection conn, long oid) throws SQLException {
        Objects.requireNonNull(conn, "conn must not be null");

        var lobj = conn.unwrap(PGConnection.class).getLargeObjectAPI();

        try (var obj = lobj.open(oid, LargeObjectManager.WRITE)) {
            var buffer = new byte[obj.size()];
            obj.read(buffer, 0, obj.size());
            return buffer;
        }
    }
}
