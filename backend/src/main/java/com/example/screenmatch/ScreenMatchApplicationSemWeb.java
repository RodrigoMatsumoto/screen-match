//package com.example.screenmatch;
//
//import com.example.screenmatch.principal.Principal;
//import com.example.screenmatch.repository.SerieRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class ScreenMatchApplicationSemWeb implements CommandLineRunner {
//
//  @Autowired
//  private SerieRepository serieRepository;
//
//  public static void main(String[] args) {
//    SpringApplication.run(ScreenMatchApplicationSemWeb.class, args);
//  }
//
//  @Override
//  public void run(String... args) throws Exception {
//    Principal principal = new Principal(serieRepository);
//    principal.exibirMenu();
//  }
//}