package io.developer.confluent;

public class ccJavaAvroClientApp {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage:");
            System.out.println("  java ProducerConsumerApp --producer <arg1> <arg2> <arg3>");
            System.out.println("  java ProducerConsumerApp --consumer <arg>");
            return;
        }
        
        String mode = args[0];
        switch (mode) {
            case "--producer":
                new ccAvroProducer().run(java.util.Arrays.copyOfRange(args, 1, args.length));
                break;
            case "--consumer":
                new ccAvroConsumer().run(java.util.Arrays.copyOfRange(args, 1, args.length));
                break;
            default:
                System.out.println("Invalid mode. Use --producer or --consumer.");
                break;
        }
    }
}
