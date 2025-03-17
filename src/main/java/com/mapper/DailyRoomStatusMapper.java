package com.mapper;

import com.exception.DeletionRejectException;
import com.pojo.DailyRoomStatus;
import com.utils.JdbcExecutor;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class DailyRoomStatusMapper implements Mapper{
    public static DailyRoomStatusMapper instance = new DailyRoomStatusMapper();
    private final static JdbcExecutor jdbcExecutor = new JdbcExecutor();
    private final static JdbcExecutor.ResultMapper resultMapper = rs -> new DailyRoomStatus(
            rs.getLong("id"),
            rs.getDate("date").toLocalDate(),
            rs.getDouble("price"),
            rs.getInt("available_count"),
            rs.getInt("booked_count"),
            rs.getLong("room_type_id")
    );

    @Override
    public void delete(Long id) throws DeletionRejectException {
        //determine count in booking
        if(BookingMapper.instance.getBookingCount(id) > 0){
            throw new DeletionRejectException("Cannot delete daily room status with booking.");
        }

        String sql = "delete from daily_room_status where id=?";
        int rows = jdbcExecutor.executeUpdate(sql, id);

        System.out.println("Deleted rows: " + rows);
    }

    public List<DailyRoomStatus> findAll() {
        String sql = "select * from daily_room_status";
        return jdbcExecutor.executeQuery(sql, resultMapper);
    }

    public List<DailyRoomStatus> findByDate(LocalDate date) {
        String sql = "SELECT * FROM daily_room_status WHERE date = ?";
        return jdbcExecutor.executeQuery(sql, resultMapper, Date.valueOf(date)); // Convert LocalDate to SQL Date
    }

    public void updatePrice(DailyRoomStatus dailyRoomStatus) {
        String sql = "update daily_room_status set price =? where id=?";
        int rows = jdbcExecutor.executeUpdate(sql, dailyRoomStatus.getPrice(), dailyRoomStatus.getId());

        System.out.println("Updated rows: " + rows);
    }

    public void insert(DailyRoomStatus dailyRoomStatus) {
        String sql = "insert into daily_room_status (date, price, available_count, booked_count, room_type_id) values(?,?,?,?,?)";
        int rows = jdbcExecutor.executeUpdate(sql, dailyRoomStatus.getDate(),dailyRoomStatus.getPrice(), dailyRoomStatus.getAvailableCount(),
                dailyRoomStatus.getBookedCount(), dailyRoomStatus.getRoomTypeId());
    }

    public int getDailyRoomStatusCount(Long roomTypeId) {
        String sql = "SELECT COUNT(*) AS count FROM daily_room_status WHERE room_type_id = ?";

        List<Integer> result = jdbcExecutor.executeQuery(sql, rs -> rs.getInt("count"), roomTypeId);

        return result.isEmpty() ? 0 : result.get(0);
    }
}
