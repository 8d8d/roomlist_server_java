package com.tencent.qcloud.roomservice.roomlist.controller;

import com.tencent.qcloud.roomservice.roomlist.pojo.Request.*;
import com.tencent.qcloud.roomservice.roomlist.pojo.Response.*;
import com.tencent.qcloud.roomservice.roomlist.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * webrtc房间接口
 */
@Controller
@ResponseBody
@RequestMapping("/weapp/webrtc_room")
public class WebRTCRoom {
    @Autowired
    RoomService roomService;

    @ResponseBody
    @RequestMapping("get_login_info")
    public GetLoginInfoRsp get_login_info(@RequestBody GetLoginInfoReq req){
        return roomService.getLoginInfo(req.getUserID());
    }

    @ResponseBody
    @RequestMapping("create_room")
    public CreateRoomRsp create_room(@RequestBody CreateRoomReq req){
        return roomService.createRoom(req);
    }

    @ResponseBody
    @RequestMapping("enter_room")
    public EnterRoomRsp enter_room(@RequestBody EnterRoomReq req){
        return roomService.enterRoom(req);
    }

    @ResponseBody
    @RequestMapping("quit_room")
    public BaseRsp quit_room(@RequestBody QuitRoomReq req){
        return roomService.quitRoom(req);
    }

    @ResponseBody
    @RequestMapping("heartbeat")
    public BaseRsp heartbeat(@RequestBody HeartBeatReq req) {
        return roomService.heartbeat(req);
    }

    @ResponseBody
    @RequestMapping("get_room_list")
    public GetRoomListRsp get_room_list(@RequestBody GetRoomListReq req) {
        return roomService.getRoomList(req);
    }

    @ResponseBody
    @RequestMapping("get_room_members")
    public GetRoomMembersRsp get_room_members(@RequestBody GetRoomMembersReq req){
        return roomService.getRoomMembers(req);
    }
}