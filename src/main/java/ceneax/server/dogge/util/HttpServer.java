package ceneax.server.dogge.util;

import java.io.*;
import java.net.Socket;

/**
 * @description: 使用socket 实现 web服务器 --- 具体执行读写操作的。
 *
 */
public class HttpServer extends Thread {
    /**
     * web资源根路径
     */
    /**
     * 输入流对象,读取浏览器请求
     */
    private InputStream input;

    /**
     * 输出流对象，响应内容给浏览器
     */
    private OutputStream out;

    /**
     * @description:初始化socket对象,获取对应 输入，输出流
     * @param socket
     */
    public HttpServer(Socket socket) {
        try {
            input = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多线程方法调用
     */
    @Override
    public void run() {
        String filePath = read();
        response(filePath);
    }

    /**
     * @description: 读取资源文件，响应给浏览器。
     * @param:@param filePath  资源文件路径
     *
     *
     */
    private void response(String filePath) {
        // 获得项目下的路径
        String path = System.getProperty("user.dir");
        File file = new File(path, filePath);
        System.out.println(filePath);
        System.out.println(path);
        System.out.println(file.getAbsolutePath());
        if (!filePath.equals("/")) {
            if (file.exists()) {
                // 1、资源存在，读取资源
                try {
                    //做个判断是否为空
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    System.out.println("reader" + reader);
                    StringBuffer sb = new StringBuffer();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\r\n");
                    }
                    StringBuffer result = new StringBuffer();
                    result.append("HTTP /1.1 200 ok \r\n");
                    result.append("Content-Type:text/html \r\n");
                    result.append("Content-Length:" + file.length() + "\r\n");
                    result.append("\r\n" + sb.toString());
                    //System.out.println("result=="+result);
                    out.write(result.toString().getBytes());
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                // 2、资源不存在，提示 file not found
                StringBuffer error = new StringBuffer();
                error.append("HTTP /1.1 400 file not found \r\n");
                error.append("Content-Type:text/html \r\n");
                error.append("Content-Length:20 \r\n").append("\r\n");
                error.append("<h1 >File Not Found..</h1>");
                try {
                    out.write(error.toString().getBytes());
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            //默认界面
            StringBuffer error = new StringBuffer();
            error.append("HTTP /1.1 200 hello word \r\n");
            error.append("Content-Type:text/html \r\n");
            error.append("Content-Length:20 \r\n").append("\r\n");
            error.append("<h1 >hello word</h1>");
            try {
                out.write(error.toString().getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @description:解析资源文件路径
     * @example: GET /index.html HTTP/1.1
     *
     *
     */
    private String read() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        try {
            // 读取请求头， 如：GET /index.html HTTP/1.1
            String readLine = reader.readLine();
            String[] split = readLine.split(" ");
            if (split.length != 3) {
                return null;
            }
            //System.out.println(readLine);
            return split[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}