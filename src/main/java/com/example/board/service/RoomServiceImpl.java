package com.example.board.service;

import com.example.board.model.Link;
import com.example.board.model.RoomResponse;
import com.example.board.model.RoomVo;
import com.example.board.persistent.RoomDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("roomService")
public class RoomServiceImpl implements RoomService {
    private final String URI_ROOMS = "http://localhost:15000/api/rooms";
    private final String URI_ROOMS_ROOMNO = "http://localhost:15000/api/rooms/{roomNo}";
    private final String URI_ROOM = "http://localhost:15000/api/room";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RoomDao roomDao;

    public List<RoomVo> retrieveRoomList(){
        return this.roomDao.selectRoomList();
    }

    //방 목록 조회
    public RoomResponse retrieveRooms() {
        ResponseEntity<RoomResponse> responseEntity = restTemplate.getForEntity(URI_ROOMS, RoomResponse.class);
        RoomResponse roomResponse = responseEntity.getBody();

        List<Link> links = roomResponse.getLinks();
        for (Link link : links) {
            System.out.println("rel : " + link.getRel());
            System.out.println("href : " + link.getHref());
        }

        List<RoomVo> content = roomResponse.getContent();
        for (RoomVo room : content) {
            System.out.println("no : " + room.getNo());
            System.out.println("roomName : " + room.getRoomName());
            System.out.println("roomDeco : " + room.getRoomDeco());
            System.out.println("roomAddress : " + room.getRoomAddress());
            System.out.println("systemFileName : " + room.getSystemFileName());
            System.out.println("originalFileName : " + room.getOriginalFileName());

            List<Link> linkList = room.getLinks();
            for (Link link : linkList) {
                System.out.println("rel : " + link.getRel());
                System.out.println("href : " + link.getHref());
            }
        }

        return roomResponse;
    }

    //방 상세 조회
    public RoomVo retrieveRoom(int roomNo){

        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("roomNo",roomNo);

        ResponseEntity<RoomVo> responseEntity = restTemplate.getForEntity(URI_ROOMS_ROOMNO, RoomVo.class, params);
        RoomVo room = new RoomVo();

        int statusCode = responseEntity.getStatusCodeValue();
        if(statusCode == 200){
            room = responseEntity.getBody();
            System.out.println("no : " + room.getNo());
            System.out.println("roomName : " + room.getRoomName());
            System.out.println("roomDeco : " + room.getRoomDeco());
            System.out.println("roomAddress : " + room.getRoomAddress());
            System.out.println("systemFileName : " + room.getSystemFileName());
            System.out.println("originalFileName : " + room.getOriginalFileName());
            List<Link> linkList = room.getLinks();
            if (linkList != null){
                for (Link link : linkList){
                    System.out.println("rel : " + link.getRel());
                    System.out.println("href : " + link.getHref());
                }
            }
        }
        return room;
    }

    //방 생성
    public String registerRoom(RoomVo room){

        URI uri = restTemplate.postForLocation(URI_ROOM, room);
        System.out.println("URI : " + uri);

        return uri.toString();
    }

    //방 삭제
    public void removeRoom(int roomNo){

        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("roomNo", roomNo);

        restTemplate.delete(URI_ROOMS_ROOMNO, params);
    }

    //방 업데이트
    public void updateRoom(RoomVo room){
        restTemplate.put(URI_ROOM, room, RoomVo.class);
    }
}
