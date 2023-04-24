package com.example.springboot.serviceimpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.example.springboot.exception.IdMismatchException;
import com.example.springboot.exception.PaymentNotValidException;
import com.example.springboot.exception.ResourceNotFoundException;
 
import com.example.springboot.model.Patient;
import com.example.springboot.model.Payment;
import com.example.springboot.model.Prescription;
import com.example.springboot.model.Room;
import com.example.springboot.repository.PatientRepository;
import com.example.springboot.repository.PaymentRepository;
 
 
import com.example.springboot.service.PatientService;
import com.example.springboot.service.PaymentService;
import com.example.springboot.service.PrescriptionService;
import com.example.springboot.service.RoomService;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;
	
	
	@Autowired
	private PatientRepository patientRepository;
	
	@Autowired
	private PatientService patientService;


//	@Autowired
//	private RoomRepository roomRepository;
	
	@Autowired
	private RoomService roomService;
	
//	@Autowired
//	private PrescriptionRepository prescriptionRepository;
	
	@Autowired
	private PrescriptionService prescriptionService;
	
	@Override
	public Payment addPayment(Payment payment ,long patientId,long prescriptionId) throws Exception {
		//BookingAppointment bA= bookingAppointmentService.getAppointmentById(appointmentId);
		
		Prescription gettingPrescription = prescriptionService.getPrescriptionById(prescriptionId);
		 
		Patient patient=patientService.getPatientById(patientId);
		
		String status=gettingPrescription.getStatus();
		
		if(status.equals("admit")) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		
		java.util.Date date=new Date();
		//date.getHours();
		System.out.println();
		//System.out.println(date.getHours());
		System.out.println(">>>>>>>>>>>" + date);
		String currentDate=sdf.format(date);
		System.out.println(">>>>>C>>>>>>"+ currentDate);
		System.out.println(currentDate);
		
		payment.setPaymentDate(date);
		
		
		
		//payment.setAppointmentId(appointmentId);
		
		payment.setPrescriptionId(gettingPrescription.getPrescriptionId());
		
		String [] array=currentDate.split("-");
		int year=Integer.parseInt(array[0]);
		System.out.println(year);
		int month=Integer.parseInt(array[1]);
		System.out.println(month);
		int day=Integer.parseInt(array[2]);
		System.out.println(day);
		
		java.util.Date admitDate=date; //patient.getAdmitDate();
		System.out.println(">>>>>>" + patient.getAdmitDate());
	//	System.out.println(admitDate.getHours());
		System.out.println();
		payment.setAdmitDate(admitDate);
		String oldDate=sdf.format(admitDate);
		System.out.println(oldDate);
		String [] array1=oldDate.split("-");
		int year1=Integer.parseInt(array1[0]);
		System.out.println(year1);
		int month1=Integer.parseInt(array1[1]);
		System.out.println(month1);
		int day1=Integer.parseInt(array1[2]);
		//System.out.println(day1);
		System.out.println(day1);
		System.out.println(day);
		int count=0;
		count=day-day1;//24-19
		
		//System.out.println(count);
		
		
		//Room room = roomRepository.findById(patient.getRoomId()).orElseThrow(()-> new ResourceNotFoundException("room","roomId",patient.getRoomId()));
		List<Payment> existingPayment = paymentRepository.findAll();
		if(existingPayment.isEmpty()) {
			
		}else {
			if(patient.getPaymentStatus().equals("paid")) {
				throw new PaymentNotValidException("Payment already has done by you ");
			}
		}
		/*Room room = roomService.getRoomById(patient.getRoomId());
		
		String type = room.getWardType();
		
		System.out.println(type); 
		
		if(type.equals("gw")) {
		    payment.setTotalPayment(count*1000);
		 }else if(type.equals("icu")) {
	    	payment.setTotalPayment(count*3000);
		}else if(type.equals("sw")) {
		    	payment.setTotalPayment(count*8000);
	}*/
		
		// payment.setRoomId(room.getRoomId());
		payment.setRoomId(1);
		 
		payment.setPatient(patient);
		patient.setDischargeDate(date);		
		
		patient.setPaymentStatus(payment.getPaymentStatus());
		
		// patient.setRoomId(0);
		
		patientRepository.save(patient);
		}else {
			payment.setPrescriptionId(gettingPrescription.getPrescriptionId());
			
			payment.setTotalPayment(300);
			 
			payment.setPatient(patient);
		
			patient.setPaymentStatus(payment.getPaymentStatus());

			payment.setPaymentDate(new Date());
			patientRepository.save(patient);
		}
		
		
	    return paymentRepository.save(payment);
	 
	}

	@Override
	public List<Payment> getAllPayments() {
		 
		return paymentRepository.findAll();
	}

	@Override
	public Payment getPaymentById(long paymentId) {
	 
		return paymentRepository.findById(paymentId).orElseThrow(()-> new ResourceNotFoundException("PaymentDetails","PaymentId",paymentId));
	}
	
	
	 
	
	

}
