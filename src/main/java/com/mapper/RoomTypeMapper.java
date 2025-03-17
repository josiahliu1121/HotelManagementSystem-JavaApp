package com.mapper;

import com.exception.DeletionRejectException;
import com.pojo.RoomType;
import com.utils.JdbcExecutor;

import java.util.List;

public class RoomTypeMapper implements Mapper {
    public static RoomTypeMapper instance = new RoomTypeMapper();
    private final static JdbcExecutor jdbcExecutor = new JdbcExecutor();

    @Override
    public void delete(Long id) throws DeletionRejectException {
        //determine count in daily_room_status
        if(DailyRoomStatusMapper.instance.getDailyRoomStatusCount(id) > 0){
            throw new DeletionRejectException("Cannot delete room type with daily status set");
        }

        String sql = "delete from room_type where id=?";
        int rows = jdbcExecutor.executeUpdate(sql, id);

        System.out.println("Deleted rows: " + rows);
    }

    public List<RoomType> findAll() {
        JdbcExecutor.ResultMapper resultMapper = rs -> new RoomType(
                rs.getLong("id"),
                rs.getString("room_name"),
                rs.getDouble("suggested_price"),
                rs.getInt("count"),
                rs.getString("description")
        );

        String sql = "select * from room_type";
        return jdbcExecutor.executeQuery(sql, resultMapper);
    }

    public void insert(RoomType roomType) {
        String sql = "insert into room_type (room_name,suggested_price,count,description) values(?,?,?,?)";
        int rows = jdbcExecutor.executeUpdate(sql, roomType.getRoomName(), roomType.getSuggestedPrice(),
                roomType.getCount(), roomType.getDescription());

        System.out.println("Inserted rows: " + rows);
    }

    public void update(RoomType roomType) {
        String sql = "update room_type set room_name=?,suggested_price=?,count=?,description=? where id=?";
        int rows = jdbcExecutor.executeUpdate(sql, roomType.getRoomName(), roomType.getSuggestedPrice(),
                roomType.getCount(), roomType.getDescription(), roomType.getId());

        System.out.println("Updated rows: " + rows);
    }
}
