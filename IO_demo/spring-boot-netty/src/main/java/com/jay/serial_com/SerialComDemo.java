package com.jay.serial_com;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
public class SerialComDemo {

    /**
     * <com名称,SerialPort>串口通信map，存储串口名称与串口信息
     */
    private final Map<String, SerialPort> comMap = new HashMap<>();
    /**
     * com口列表
     */
    private final List<String> comList = new ArrayList<>();

    @PostConstruct
    private void init(){
        //将所有的串口信息放入comList,comMap中
        SerialPort[] commPorts = SerialPort.getCommPorts();
        for (SerialPort commPort : commPorts) {
            log.info("窗口名称："+commPort.getSystemPortName());
            comList.add(commPort.getSystemPortName());
            comMap.put(commPort.getSystemPortName(), commPort);
            //监听所有串口通信的数据
            commPort.openPort();
            commPort.addDataListener(new SerialPortDataListener() {
                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
                }
                @Override
                public void serialEvent(SerialPortEvent serialPortEvent) {
                    byte[] newData = serialPortEvent.getReceivedData();
                    log.info("com {} receive data size：{},com data:{}",
                            serialPortEvent.getSerialPort().getSystemPortName(),
                            newData.length,new String(newData));
                    // 开始经控制器发送信息
//                    Socket socket = SocketServer.ONLINE_SOCKET_MAP.get(new String(newData));
                    Socket socket = null;
                    try {
                        socket = new Socket("127.0.0.1", 8008);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (socket != null){
                        OutputStream os = null;
                        try {
                            os = socket.getOutputStream();
                            for (int i = 0; i < 10; i++) {
                                os.write("pt01,1.0,1.0,1.0,正常".getBytes(Charset.forName("GBK")));
                                os.flush();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        }
    }

    public String sendInfoToSerial(String com, String control){
        if(!comList.contains(com)){
            log.error("串口{}不存在", com);
            return "串口不存在";
        }
        SerialPort serialPort = comMap.get(com);
        log.info("控制器:{},向串口:{}发送了一条信息",control, com);
        serialPort.writeBytes(control.getBytes(),control.getBytes().length);
        serialPort.flushIOBuffers();
        return "发送串口信息成功";
    }
}
