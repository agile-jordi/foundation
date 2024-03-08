package com.agilogy.jdbc;

import java.sql.SQLException;

@FunctionalInterface
public interface RowReader<A> {
  A apply(ResultSetView rs) throws SQLException;
}
