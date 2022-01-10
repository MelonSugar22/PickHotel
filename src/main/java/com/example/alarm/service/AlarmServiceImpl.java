package com.example.alarm.service;

import com.example.alarm.model.AlarmVo;
import com.example.alarm.persistent.AlarmDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("alarmService")
public class AlarmServiceImpl implements AlarmService {

    @Autowired
    private AlarmDao alarmDao;

    // 알림을 발송한다
    @Override
    public void registerAlarm(AlarmVo alarm) {
        this.alarmDao.insertAlarm(alarm);
    }

    // 회원의 알림 목록을 조회한다.
    @Override
    public List<AlarmVo> retrievAlarmList(int memNo) {

        //회원이 알림 목록을 전체 조회한다.
        List<AlarmVo> alarms = this.alarmDao.selectAlarmList(memNo);

        //조회와 동시에 알림 읽음 처리가 된다.
        this.alarmDao.updateReadAlarmList(memNo);
        //성공시 1, 실패시 0

        return alarms;
    }

    // 회원의 미열람 상태인 알림의 개수를 조회한다.
    @Override
    public int retrieveNoReadAlarmCount(int memNo) {
        int result = this.alarmDao.selectNoReadAlarmCount(memNo);
        return result;
    }

    // 조건에 해당하는 알림을 삭제한다.
    @Override
    public void removeAlarm(int alarmNo) {
        this.alarmDao.delectAlarm(alarmNo);
    }

    // 조건에 해당하는 알림을 전체 삭제한다.
    @Override
    public void removeAllAlarm(int memNo) {
        this.alarmDao.delectAllAlarm(memNo);
    }

}
