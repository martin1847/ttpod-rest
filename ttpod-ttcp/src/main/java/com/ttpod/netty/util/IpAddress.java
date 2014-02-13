package com.ttpod.netty.util;

import java.net.*;
import java.util.Enumeration;

/**
 * date: 14-2-13 下午6:03
 *
 * @author: yangyang.cong@ttpod.com
 */
public abstract class IpAddress {


    public static void main(String[] args) {
        System.out.println(eth(0));
    }

    public static String eth(int number){
        try {
            Enumeration<NetworkInterface> netInterfaces  = NetworkInterface.getNetworkInterfaces();
            String name = "eth" + number;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface iface = netInterfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                if(name.equals(iface.getName())){
                    Enumeration<InetAddress> ips = iface.getInetAddresses();
                    while (ips.hasMoreElements()) {
                        InetAddress addr = ips.nextElement();
                        if (addr instanceof Inet6Address){//ignored IPV6
                            continue;
                        }
                        return  addr.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String eth0IpOrHostName(){
        String ipv4 = eth(0);
        if(null == ipv4){
            try {
                ipv4 = InetAddress.getLocalHost().getCanonicalHostName();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                throw new RuntimeException("UnknownHostException " ,e);
            }
        }
        return ipv4;
    }


}
