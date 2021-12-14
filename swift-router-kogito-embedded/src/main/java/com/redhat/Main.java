
package com.redhat;


import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.runtime.Quarkus;

import com.redhat.SwiftApp;


@QuarkusMain  
public class Main {

    public static void main(String ... args) {
        System.out.println("Running main method");
        Quarkus.run(SwiftApp.class); 
    }
}
