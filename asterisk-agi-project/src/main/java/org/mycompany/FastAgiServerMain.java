/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mycompany;

import org.asteriskjava.fastagi.DefaultAgiServer;
import org.asteriskjava.fastagi.MappingStrategy;
import org.asteriskjava.fastagi.ResourceBundleMappingStrategy;

/**
 *
 * @author syousrei
 */
public class FastAgiServerMain {
    public static void main(String[] args) throws Exception {
        MappingStrategy strategy = new ResourceBundleMappingStrategy();
        DefaultAgiServer server = new DefaultAgiServer(strategy);
        server.startup();
        System.out.println("FastAGI server started and listening on port 4573...");
    }
}
