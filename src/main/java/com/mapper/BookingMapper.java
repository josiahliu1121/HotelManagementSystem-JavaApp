package com.mapper;

import com.exception.DeletionRejectException;
import com.pojo.Booking;
import com.utils.JdbcExecutor;

import java.time.LocalDate;
import java.util.List;

public class BookingMapper implements Mapper{
    private static final JdbcExecutor jdbcExecutor = new JdbcExecutor();
    public static BookingMapper instance = new BookingMapper();
    private final static JdbcExecutor.ResultMapper resultMapper = rs -> new Booking(
            rs.getLong("id"),
            rs.getString("customer_name"),
            rs.getString("customer_phone"),
            rs.getDouble("cost"),
            rs.getDate("booking_date").toLocalDate(),
            rs.getInt("adult"),
            rs.getInt("child"),
            rs.getString("remarks"),
            rs.getLong("daily_room_status_id"),
            rs.getLong("room_type_id"),
            rs.getLong("employee_id"),
            rs.getTimestamp("create_time").toLocalDateTime(),
            rs.getTimestamp("update_time").toLocalDateTime()
    );

    @Override
    public void delete(Long id) {
        //update the data in daily room status
        Booking booking = findById(id);
        DailyRoomStatusMapper.instance.deleteBooking(booking.getDailyRoomStatusId());

        String sql = "delete from booking where id=?";
        int rows = jdbcExecutor.executeUpdate(sql, id);

        System.out.println("Deleted rows: " + rows);
    }

    public int getBookingCount(Long dailyRoomStatusId) {
        String sql = "select count(*) as count from booking where daily_room_status_id=?";

        List<Integer> result = jdbcExecutor.executeQuery(sql, rs -> rs.getInt("count"), dailyRoomStatusId);

        return result.isEmpty() ? 0 : result.get(0);
    }

    public List<Booking> findAll() {
        String sql = "select * from booking";
        return jdbcExecutor.executeQuery(sql, resultMapper);
    }

    public Booking findById(Long id) {
        String sql = "select * from booking where id=?";

        List<Booking> result = jdbcExecutor.executeQuery(sql, resultMapper, id);
        return result.isEmpty() ? null : result.get(0);
    }

    public List<Booking> findByDate(LocalDate localDate) {
        String sql = "select * from booking where booking_date=?";
        return jdbcExecutor.executeQuery(sql, resultMapper, localDate);
    }

    public void insert(Booking booking) {
        String sql = "INSERT INTO booking (customer_name, customer_phone, cost, booking_date, adult, child, remarks, " +
                "daily_room_status_id, room_type_id, employee_id, create_time, update_time) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int rows = jdbcExecutor.executeUpdate(sql,
                booking.getCustomerName(),
                booking.getCustomerPhone(),
                booking.getCost(),
                booking.getBookingDate(),
                booking.getAdult(),
                booking.getChild(),
                booking.getRemarks(),
                booking.getDailyRoomStatusId(),
                booking.getRoomTypeId(),
                booking.getEmployeeId(),
                booking.getCreateTime(),
                booking.getUpdateTime()
        );

        System.out.println("Inserted rows: " + rows);
    }

    public void update(Booking booking) {
        String sql = "UPDATE booking SET customer_name=?, customer_phone=?, cost=?, adult=?, child=?, " +
                "remarks=?, employee_id=?, update_time=? WHERE id=?";

        int rows = jdbcExecutor.executeUpdate(sql,
                booking.getCustomerName(),
                booking.getCustomerPhone(),
                booking.getCost(),
                booking.getAdult(),
                booking.getChild(),
                booking.getRemarks(),
                booking.getEmployeeId(),
                booking.getUpdateTime(),
                booking.getId()
        );

        System.out.println("Updated rows: " + rows);
    }
}
