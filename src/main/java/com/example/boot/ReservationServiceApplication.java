package com.example.boot;

import java.util.Collection;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class ReservationServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ReservationServiceApplication.class, args);
	}
	
	@Component
	class SampleDataCLR implements CommandLineRunner{
		
		@Autowired
		SampleDataCLR(ReservationRepository reservationRepository){
			this.reservationRepository = reservationRepository;
		}
				
		private final ReservationRepository reservationRepository;
		
		

		@Override
		public void run(String... arg0) throws Exception {
			Stream.of("Josh","Shahbaz","Bob","Venkat").forEach(name -> reservationRepository.save(new Reservation(name)));
			reservationRepository.findAll().forEach(System.out::println);
			
		}
		
	}
	
}


@Component
class CustomHealthIndicator implements HealthIndicator{

	@Override
	public Health health() {
		return Health.status("I do it my way").build();
	}
	
}

	@RepositoryRestResource
	interface ReservationRepository extends JpaRepository<Reservation, Long>	{
		
		@RestResource(path="by-name")
		Collection<Reservation> findByReservationName(@Param("rn")String rn);
		
	}
	
	
	@Entity
	class Reservation{
		
		@Id
		@GeneratedValue
		private Long id;
		
		
		private String reservationName;

		public Reservation(){
			
		}

		
		public String getReservationName() {
			return reservationName;
		}


		public void setReservationName(String reservationName) {
			this.reservationName = reservationName;
		}


		public Reservation(String reservationName) {
			this.reservationName = reservationName;
		}

		@Override
		public String toString() {
			return "Reservation [id=" + id + ", reservationName=" + reservationName + "]";
		}
		
		
		
	}

