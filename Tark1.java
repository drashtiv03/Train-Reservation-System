import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.*;

import javax.xml.transform.Source;

import java.time.format.*;

public class Tark1{
    
        public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        String trainnum1="17726";
        String trainnum2="37392";
        String trainnum3="29772";
        String[] stop1={"Rajkot","Ahmedabad","Vadodra","Surat","Mumbai"};
        String[] stop2={"Ahmedabad", "Anand", "Vadodara","Bharuch","Surat"};
        String[] stop3={"Vadodra","Dahod","Indore"};
        int[] dist1={0,200,300,500,750};
        int[] dist2={0,50,100,200,300};
        int[] dist3={0,150,350};
        String[] coach1={"S1","S2","B1","A1","H1"};
        String[] coach2={"S1","S2","S3","B1","B2"};
        String[] coach3={"S1","S2","B1","A1"};
        int[] numofseats1={72,72,72,48,24};
        int[] numofseats2={15,20,50,36,48};
        int[] numofseats3={72,72,72,48};
        int ticknum=10000001;
        Train[] trains = new Train[3];
       trains[0]=new Train(trainnum1,stop1,dist1,coach1,numofseats1);
       trains[1]=new Train(trainnum2,stop2,dist2,coach2,numofseats2);
       trains[2]=new Train(trainnum3,stop3,dist3,coach3,numofseats3);
        Map<Integer,String>pnr=new HashMap<>();
        Map<Integer,String>report=new HashMap<>();
       
        while (true)
        {
            System.out.println("Write BOOK for booking");
            System.out.println("Write PNR for booked ticket details");
            System.out.println("Write REPORT for report of booked tickets");
            System.out.println("Write EXIT to exit");
            String in=sc.nextLine();

            if(in.equals("EXIT"))
            {
                System.out.println("Thank You for choosing Indian Railways");
                System.out.println("We hope to serve you again!");
                break;
            }
             else if(in.equals("REPORT"))
            {
                if(ticknum!=10000001)
                {
                    for (Integer key : report.keySet()) {
                        String value = report.get(key);
                        System.out.println(key + " " + value);
                    }
                }
                else
                {
                    System.out.println("No booked tickets found! Book a ticket to get data!");
                }
            System.out.println();
            }
            else if(in.equals("PNR"))
            {
                System.out.println("Enter the pnr number of the ticket");
                Integer pnrid=sc.nextInt();
                if(ticknum==10000001)
                {
                    System.out.println("No booked tickets found.Book a ticket now!");
                }
                else
                {
                    if(pnr.containsKey(pnrid))
                    {
                        System.out.println(pnrid+" "+pnr.get(pnrid));
                    } 
                    else
                    {
                        System.out.println("There is no such PNR for booked ticket");
                    }
                }
            }
            else if(in.equals("BOOK"))
            {
            List<Seat>booked=new ArrayList<>();
            System.out.println("Enter the input");
            String input=sc.nextLine();
            String[] inputs=input.split(" ");
            String source=inputs[0];
            String dest=inputs[1];
            LocalDate inputdate=LocalDate.parse(inputs[2]);
            String coa=inputs[3];
            int num=Integer.parseInt(inputs[4]);
            int num1=num;
            boolean flag=false,flag1=false;
            City sourceCity=new City();
            City destCity=new City();
            for(Train train:trains)
            {
                for (City city : train.cities) 
                {
                
                    if(source.equals(city.name))
                    {
                        flag=true;
                        sourceCity=city;
                        
                    }
                    else if(dest.equals(city.name) && flag==true && train.check(num,coa,inputdate,sourceCity,destCity)==true)
                    {
                        flag1=true;
                        System.out.println(train.trainno);
                        destCity=city;
                        break;
                    }
                }
            }
            if(flag1==true)
            {
                System.out.println("Enter the train you want to book");
                String trainnum=sc.nextLine();
                for(Train train:trains)
                {
                    if(train.trainno.equals(trainnum))
                    {
                        String st="";
                        outerloop:
                        for(Coach coach:train.coaches)
                        {
                            if(coach.coachType.equals(CoachType.Sleeper) && coa.startsWith("S"))
                            {
                                for(Seat seat:coach.seats)
                                {
                                    if(num==0)
                                    {
                                        break outerloop;
                                    }
                                    else
                                    {
                                        boolean dtfound=false;
                                        for(ReservedDate reservedate:seat.ReservedDates)
                                        {
                                            if(reservedate.date.equals(inputdate))
                                            {
                                                dtfound=true;
                                                boolean locfound=false;
                                                for(ReservedLoc reservedloc:reservedate.reservedLocs)
                                                {
                                                    if((reservedloc.source.equals(sourceCity)||reservedloc.destination.equals(destCity))||((reservedloc.source.dist<sourceCity.dist && reservedloc.destination.dist>sourceCity.dist) || (reservedloc.source.dist<destCity.dist && reservedloc.destination.dist>destCity.dist))||((reservedloc.source.dist>sourceCity.dist && reservedloc.source.dist<destCity.dist)||(reservedloc.destination.dist>sourceCity.dist && reservedloc.destination.dist<destCity.dist)))
                                                    {
                                                        locfound=true;
                                                        break;
                                                    }
                                                }
                                                if(!locfound)
                                                {
                                                    st+=seat.seatNo+" ";
                                                    booked.add(seat);
                                                    num--;
                                                    reservedate.reservedLocs.add(new ReservedLoc(sourceCity,destCity));
                                                }
                                                break;
                                            }
                                        }
                                            if(!dtfound)
                                            {
                                                ReservedDate reserv=new ReservedDate();
                                                reserv.date=inputdate;
                                                reserv.reservedLocs=new ArrayList<>();
                                                reserv.reservedLocs.add(new ReservedLoc(sourceCity, destCity));
                                                seat.ReservedDates.add(reserv);
                                                st+=seat.seatNo+" ";
                                                booked.add(seat);
                                                num--;
                                            }
                                    }
                                }
                            
                            } 
                            else if(coach.coachType.equals(CoachType.TIER_3) && coa.startsWith("3"))
                            {
                                for(Seat seat:coach.seats)
                                {
                                    if(num==0)
                                    {
                                        break outerloop;
                                    }
                                    else
                                    {
                                        boolean dtfound=false;
                                        for(ReservedDate reservedate:seat.ReservedDates)
                                        {
                                            if(reservedate.date.equals(inputdate))
                                            {
                                                dtfound=true;
                                                boolean locfound=false;
                                                for(ReservedLoc reservedloc:reservedate.reservedLocs)
                                                {
                                                    if((reservedloc.source.equals(sourceCity)||reservedloc.destination.equals(destCity))||((reservedloc.source.dist<sourceCity.dist && reservedloc.destination.dist>sourceCity.dist) || (reservedloc.source.dist<destCity.dist && reservedloc.destination.dist>destCity.dist))||((reservedloc.source.dist>sourceCity.dist && reservedloc.source.dist<destCity.dist)||(reservedloc.destination.dist>sourceCity.dist && reservedloc.destination.dist<destCity.dist)))
                                                    {
                                                        locfound=true;
                                                        break;
                                                    }
                                                }
                                                if(!locfound)
                                                {
                                                    booked.add(seat);
                                                    st+=seat.seatNo+" ";
                                                    num--;
                                                    reservedate.reservedLocs.add(new ReservedLoc(sourceCity,destCity));
                                                }
                                                break;
                                            }
                                        }
                                            if(!dtfound)
                                            {
                                                ReservedDate reserv=new ReservedDate();
                                                reserv.date=inputdate;
                                                reserv.reservedLocs=new ArrayList<>();
                                                reserv.reservedLocs.add(new ReservedLoc(sourceCity, destCity));
                                                seat.ReservedDates.add(reserv);
                                                booked.add(seat);
                                                st+=seat.seatNo+" ";
                                                num--;
                                            }
                                    }
                                }
                            }
                            else if(coach.coachType.equals(CoachType.TIER_2) && coa.startsWith("2"))
                            {
                                for(Seat seat:coach.seats)
                                {
                                    if(num==0)
                                    {
                                        break outerloop;
                                    }
                                    else
                                    {
                                        boolean dtfound=false;
                                        for(ReservedDate reservedate:seat.ReservedDates)
                                        {
                                            if(reservedate.date.equals(inputdate))
                                            {
                                                dtfound=true;
                                                boolean locfound=false;
                                                for(ReservedLoc reservedloc:reservedate.reservedLocs)
                                                {
                                                    if((reservedloc.source.equals(sourceCity)||reservedloc.destination.equals(destCity))||((reservedloc.source.dist<sourceCity.dist && reservedloc.destination.dist>sourceCity.dist) || (reservedloc.source.dist<destCity.dist && reservedloc.destination.dist>destCity.dist))||((reservedloc.source.dist>sourceCity.dist && reservedloc.source.dist<destCity.dist)||(reservedloc.destination.dist>sourceCity.dist && reservedloc.destination.dist<destCity.dist)))
                                                    {
                                                        locfound=true;
                                                        break;
                                                    }
                                                }
                                                if(!locfound)
                                                {
                                                    booked.add(seat);
                                                    st+=seat.seatNo+" ";
                                                    num--;
                                                    reservedate.reservedLocs.add(new ReservedLoc(sourceCity,destCity));
                                                }
                                                break;
                                            }
                                        }
                                            if(!dtfound)
                                            {
                                                ReservedDate reserv=new ReservedDate();
                                                reserv.date=inputdate;
                                                reserv.reservedLocs=new ArrayList<>();
                                                reserv.reservedLocs.add(new ReservedLoc(sourceCity, destCity));
                                                seat.ReservedDates.add(reserv);
                                                booked.add(seat);
                                                st+=seat.seatNo+" ";
                                                num--;
                                            }
                                    }
                                }
                            }  
                            else if(coach.coachType.equals(CoachType.TIER_1) && coa.startsWith("1"))
                            {
                                for(Seat seat:coach.seats)
                                {
                                    if(num==0)
                                    {
                                        break outerloop;
                                    }
                                    else
                                    {
                                        boolean dtfound=false;
                                        for(ReservedDate reservedate:seat.ReservedDates)
                                        {
                                            if(reservedate.date.equals(inputdate))
                                            {
                                                dtfound=true;
                                                boolean locfound=false;
                                                for(ReservedLoc reservedloc:reservedate.reservedLocs)
                                                {
                                                    if((reservedloc.source.equals(sourceCity)||reservedloc.destination.equals(destCity))||((reservedloc.source.dist<sourceCity.dist && reservedloc.destination.dist>sourceCity.dist) || (reservedloc.source.dist<destCity.dist && reservedloc.destination.dist>destCity.dist))||((reservedloc.source.dist>sourceCity.dist && reservedloc.source.dist<destCity.dist)||(reservedloc.destination.dist>sourceCity.dist && reservedloc.destination.dist<destCity.dist)))
                                                    {
                                                        locfound=true;
                                                        break;
                                                    }
                                                }
                                                if(!locfound)
                                                {
                                                    booked.add(seat);
                                                    st+=seat.seatNo+" ";
                                                    num--;
                                                    reservedate.reservedLocs.add(new ReservedLoc(sourceCity,destCity));
                                                }
                                                break;
                                            }
                                        }
                                            if(!dtfound)
                                            {
                                                ReservedDate reserv=new ReservedDate();
                                                reserv.date=inputdate;
                                                reserv.reservedLocs=new ArrayList<>();
                                                reserv.reservedLocs.add(new ReservedLoc(sourceCity, destCity));
                                                seat.ReservedDates.add(reserv);
                                                booked.add(seat);
                                                st+=seat.seatNo+" ";
                                                num--;
                                            }
                                    }
                                }
                            } 
                        }
                        
                        System.out.println(ticknum);
                        String s1="",s2="";
                        s1+=String.valueOf(trainnum)+" "+sourceCity.name+" "+destCity.name+" "+String.valueOf(inputdate)+" ";
                        s2+=String.valueOf(inputdate)+" "+String.valueOf(trainnum)+" "+sourceCity.name+" "+destCity.name+" ";
                        if(coa.startsWith("S"))
                        {
                            System.out.println(CoachType.Sleeper.calculateFare(Math.abs(sourceCity.dist-destCity.dist), num1));
                            s1+=CoachType.Sleeper.calculateFare(Math.abs(sourceCity.dist-destCity.dist), num1);
                            s2+=CoachType.Sleeper.calculateFare(Math.abs(sourceCity.dist-destCity.dist), num1);
                            
                           
                        }
                        else if(coa.startsWith("3"))
                        {
                            System.out.println(CoachType.TIER_3.calculateFare(Math.abs(sourceCity.dist-destCity.dist), num1));
                            s1+=CoachType.TIER_3.calculateFare(Math.abs(sourceCity.dist-destCity.dist), num1);
                            s2+=CoachType.TIER_3.calculateFare(Math.abs(sourceCity.dist-destCity.dist), num1);
                           
                        }
                        else if(coa.startsWith("2"))
                        {
                            System.out.println(CoachType.TIER_2.calculateFare(Math.abs(sourceCity.dist-destCity.dist), num1));
                            s1+=CoachType.TIER_2.calculateFare(Math.abs(sourceCity.dist-destCity.dist), num1);
                            s2+=CoachType.TIER_2.calculateFare(Math.abs(sourceCity.dist-destCity.dist), num1);
                           
                        }
                        else if(coa.startsWith("1"))
                        {
                            System.out.println(CoachType.TIER_1.calculateFare(Math.abs(sourceCity.dist-destCity.dist), num1));
                            s1+=CoachType.TIER_1.calculateFare(Math.abs(sourceCity.dist-destCity.dist), num1);
                            s2+=CoachType.TIER_1.calculateFare(Math.abs(sourceCity.dist-destCity.dist), num1);
                        }
                        s1=s1+" "+st;
                        s2=s2+" "+st;
                        pnr.put(ticknum,s1);
                        report.put(ticknum,s2);
                       System.out.println("Ticket Booked Successfully");
                        ticknum++;
                    }
                }
            }
            else
            {
                System.out.println("Try booking seats with a lesser number");
            }
        }
        else
        {
            System.out.println("Please enter a valid keyword from given instructions");
        }
        }
    }  

    }
    



class City {
    String name;
    int dist;
}
enum CoachType{
    Sleeper(1),TIER_3(2),TIER_2(3),TIER_1(4);
    int fareperkm;
    CoachType(int fare)
    {
        this.fareperkm=fare;
    }
    int calculateFare(int distance,int passenger)
    {
        return this.fareperkm*distance*passenger;
    }
}
class Coach{
    String coachName;
    CoachType coachType;
    List<Seat>seats;
    void getCoachType(String coachName)
    {
        if (coachName.charAt(0)== 'S')
        {
            this.coachType=CoachType.Sleeper;
        }
        else if (coachName.charAt(0)== 'B')
        {
            this.coachType=CoachType.TIER_3;
        }
        else if (coachName.charAt(0)== 'A')
        {
            this.coachType=CoachType.TIER_2;
        }
        else
        {
            this.coachType=CoachType.TIER_1;
        }
    }
}
class Train{
    String trainno;
    List<City> cities = new ArrayList<>();
    List<Coach>coaches=new ArrayList<>();
    Train(String trainno,String[] stop,int[] dist,String[] coach,int[] numofseats)
    {
        this.trainno=trainno;
        for(int i=0;i<stop.length;i++)
        {
            City city=new City();
            city.name=stop[i];
            city.dist=dist[i];
            cities.add(city);
        }
        for(int i=0;i<coach.length;i++)
        {
            Coach coache=new Coach();
            String coachName=coach[i];
            coache.coachName=coachName;
            coache.getCoachType(coachName);
            coache.seats=new ArrayList<>();
            for(int k=1;k<=numofseats[i];k++)
            {
                Seat seat=new Seat();
                seat.seatNo=coache.coachName+"-"+k;
                coache.seats.add(seat);
                seat.ReservedDates=new ArrayList<>();
            }
            coaches.add(coache);
        }
    }
    String[] booking(int num,String source,String dest,LocalDate date,int fare)
    {
        String[] str=new String[5];
        str[0]=String.valueOf(num);
        str[1]=source;
        str[2]=dest;
        str[3]=String.valueOf(date);
        str[4]=String.valueOf(fare);
        return str;
    }
   
    boolean check(int num,String coa,LocalDate inputdate,City sourceCity,City destCity)
    {
        int c=0,num1=num;
        outerloop:
        for(Coach coach:coaches)
           {
                if(coach.coachType.equals(CoachType.Sleeper) && coa.startsWith("S"))
                {
                    for(Seat seat:coach.seats)
                    {
                       
                        if(num<=0)
                        {
                           break outerloop;
                        }
                        
                                boolean dtfound=false;
                                for(ReservedDate reservedate:seat.ReservedDates)
                                {
                                    
                                    if(reservedate.date.equals(inputdate))
                                    {
                                        dtfound=true;
                                        boolean locfound=false;
                                        for(ReservedLoc reservedloc:reservedate.reservedLocs)
                                        {
                                            if((reservedloc.source.equals(sourceCity)||reservedloc.destination.equals(destCity))||((reservedloc.source.dist<sourceCity.dist && reservedloc.destination.dist>sourceCity.dist) || (reservedloc.source.dist<destCity.dist && reservedloc.destination.dist>destCity.dist))||((reservedloc.source.dist>sourceCity.dist && reservedloc.source.dist<destCity.dist)||(reservedloc.destination.dist>sourceCity.dist && reservedloc.destination.dist<destCity.dist)))
                                            {
                                                locfound=true;
                                                break;
                                            }
                                        }
                                        if(!locfound)
                                        {
                                            c++;
                                            num--;
                                        }
                                        break;
                                    }
                                }
                                if(!dtfound)
                                {
                                    c++;
                                    num--;
                                }
                            }
                        }
                else if(coach.coachType.equals(CoachType.TIER_3) && coa.startsWith("3"))
                    {
                        for(Seat seat:coach.seats)
                        {
                        
                            if(num<=0)
                            {
                            break outerloop;
                            }
                            
                                    boolean dtfound=false;
                                    for(ReservedDate reservedate:seat.ReservedDates)
                                    {
                                        
                                        if(reservedate.date.equals(inputdate))
                                        {
                                            dtfound=true;
                                            boolean locfound=false;
                                            for(ReservedLoc reservedloc:reservedate.reservedLocs)
                                            {
                                                if((reservedloc.source.equals(sourceCity)||reservedloc.destination.equals(destCity))||((reservedloc.source.dist<sourceCity.dist && reservedloc.destination.dist>sourceCity.dist) || (reservedloc.source.dist<destCity.dist && reservedloc.destination.dist>destCity.dist))||((reservedloc.source.dist>sourceCity.dist && reservedloc.source.dist<destCity.dist)||(reservedloc.destination.dist>sourceCity.dist && reservedloc.destination.dist<destCity.dist)))
                                                {
                                                    locfound=true;
                                                    break;
                                                }
                                            }
                                            if(!locfound)
                                            {
                                                c++;
                                                num--;
                                            }
                                            break;
                                        }
                                    }
                                    if(!dtfound)
                                    {
                                        c++;
                                        num--;
                                    }
                                }
                            }
                else if(coach.coachType.equals(CoachType.TIER_2) && coa.startsWith("2"))
                    {
                        for(Seat seat:coach.seats)
                        {
                        
                            if(num<=0)
                            {
                            break outerloop;
                            }
                            
                                    boolean dtfound=false;
                                    for(ReservedDate reservedate:seat.ReservedDates)
                                    {
                                        
                                        if(reservedate.date.equals(inputdate))
                                        {
                                            dtfound=true;
                                            boolean locfound=false;
                                            for(ReservedLoc reservedloc:reservedate.reservedLocs)
                                            {
                                                if((reservedloc.source.equals(sourceCity)||reservedloc.destination.equals(destCity))||((reservedloc.source.dist<sourceCity.dist && reservedloc.destination.dist>sourceCity.dist) || (reservedloc.source.dist<destCity.dist && reservedloc.destination.dist>destCity.dist))||((reservedloc.source.dist>sourceCity.dist && reservedloc.source.dist<destCity.dist)||(reservedloc.destination.dist>sourceCity.dist && reservedloc.destination.dist<destCity.dist)))
                                                {
                                                    locfound=true;
                                                    break;
                                                }
                                            }
                                            if(!locfound)
                                            {
                                                c++;
                                                num--;
                                            }
                                            break;
                                        }
                                    }
                                    if(!dtfound)
                                    {
                                        c++;
                                        num--;
                                    }
                                }
                            }
                else if(coach.coachType.equals(CoachType.TIER_1) && coa.startsWith("1"))
                    {
                        for(Seat seat:coach.seats)
                        {
                        
                            if(num<=0)
                            {
                            break outerloop;
                            }
                            
                                    boolean dtfound=false;
                                    for(ReservedDate reservedate:seat.ReservedDates)
                                    {
                                        
                                        if(reservedate.date.equals(inputdate))
                                        {
                                            dtfound=true;
                                            boolean locfound=false;
                                            for(ReservedLoc reservedloc:reservedate.reservedLocs)
                                            {
                                                if((reservedloc.source.equals(sourceCity)||reservedloc.destination.equals(destCity))||((reservedloc.source.dist<sourceCity.dist && reservedloc.destination.dist>sourceCity.dist) || (reservedloc.source.dist<destCity.dist && reservedloc.destination.dist>destCity.dist))||((reservedloc.source.dist>sourceCity.dist && reservedloc.source.dist<destCity.dist)||(reservedloc.destination.dist>sourceCity.dist && reservedloc.destination.dist<destCity.dist)))
                                                {
                                                    locfound=true;
                                                    break;
                                                }
                                            }
                                            if(!locfound)
                                            {
                                                c++;
                                                num--;
                                            }
                                            break;
                                        }
                                    }
                                    if(!dtfound)
                                    {
                                        c++;
                                        num--;
                                    }
                                }
                            }
                        }
        return c==num1;
        }
       
}
class Seat{
    String seatNo;
    List<ReservedDate>ReservedDates;
}  
class ReservedDate{
    LocalDate date;
    List<ReservedLoc>reservedLocs;
    
}
class ReservedLoc{
    City source;
    City destination;
    ReservedLoc(City source,City Dest)
    {
        this.source=source;
        this.destination=Dest;

    }
}
