package com.appjishu.fpush.core.app;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import com.appjishu.fpush.core.proto.FBody;
import com.appjishu.fpush.core.proto.FHeader;
import com.appjishu.fpush.core.proto.FMessage;


public class FpushCoreApp {
    private static String path = "D:\\git\\flylib\\fpush\\fpush-core\\target\\OUT.txt";

    public static FMessage newTestMessage() {
    	FMessage.Builder builder = FMessage.newBuilder();
    	FHeader.Builder headerBuilder = FHeader.newBuilder();
    	headerBuilder.setAlias("11101");
    	headerBuilder.setType(5);
    	headerBuilder.setSessionId(1234);
    	headerBuilder.setAccount(null);
    	headerBuilder.setPriority(9);
    	builder.setHeader(headerBuilder.build());
    	FBody.Builder fBodyBuilder = FBody.newBuilder();
    	fBodyBuilder.setTitle("TestTitle");
    	fBodyBuilder.setDescription("This is a Description");
    	fBodyBuilder.setExtra("{\"k\":\"v\"}");
    	builder.setBody(fBodyBuilder.build());
    	return builder.build();
    }

//    public static void main(String[] args) throws Exception {
//    	writeTest();
//    	readTest();
//    }
    
    private static void writeTest() throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        FMessage fMessage = newTestMessage();
        fMessage.writeTo(fileOutputStream);
        fileOutputStream.close();
        System.out.println("---writeTest_done--->");
    }

    private static void readTest() throws Exception {
    	FMessage readObj = FMessage.parseFrom(new FileInputStream(path));
    	FHeader header = readObj.getHeader();
    	
    	System.out.println("header.getAlias:" + header.getAlias());
    	System.out.println("header.getAccount:" + header.getAccount());
    	System.out.println("header.getSessionId:" + header.getSessionId());
    	System.out.println("header.getType:" + header.getType());
    	System.out.println("header.getPriority:" + header.getPriority());
    	System.out.println("body:" + readObj.getBody());
    	System.out.println("----------------header done-------------");
    	byte[] bytes = readObj.toByteArray();
    	System.out.println("Parsed bytes=" + bytes);
    }
}