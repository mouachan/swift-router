
package com.redhat.rest.client;


import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.runtime.Quarkus;

import com.redhat.rest.client.SwiftApp;


@QuarkusMain  
public class Main {

    public static void main(String ... args) {
        System.out.println("Running main method");
        Quarkus.run(SwiftApp.class); 
    }
}
