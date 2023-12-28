package at.jku.dke.etutor.modules.drools;

import at.jku.dke.etutor.modules.drools.analysis.DroolsAnalysis;

import java.io.IOException;

public class DroolsTestApplication {

    public static void main(String[] args) throws IOException, ReflectiveOperationException {

        DroolsAnalysis analysis = new DroolsAnalysis(1,RULES);

        analysis.hasSyntaxError();

        analysis.createSampleSolution(true);
        analysis.createSampleSolution(false);

        System.out.println(analysis.toString());



    }

    private static final String RULES = """
            package at.jku.dke.etutor.modules.drools.jit;

            // Imports nicht notwendig, da gleiches package
            // hier nur damit Auto-Complete zumindest tlw. funktioniert
            import at.jku.dke.etutor.modules.drools.jit.EnterParkingLotEvent
            import at.jku.dke.etutor.modules.drools.jit.ExitParkingLotEvent

            // Konsolenausgaben sind nicht erforderlich; hier nur zum Debugging
            rule "Combine parking intervals if reentry within 15 min"
            when
                $enter1 : EnterParkingLotEvent()
                $exit : ExitParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after $enter1)
                $enter2 : EnterParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after[0s, 15m] $exit)
            then
                System.out.printf("Re-enter within 15 minutes after exiting [vehicle: %s, enter1: %s, exit: %s, enter2: %s]%n",
                    $enter1.vehicle().licensePlate(),
                    $enter1.timestamp(),
                    $exit.timestamp(),
                    $enter2.timestamp());
                delete($exit);
                delete($enter2);
            end

            rule "Do not combine parking intervals if reentry after 15 min"
            when
                $enter1 : EnterParkingLotEvent()
                $exit : ExitParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after $enter1)
                $enter2 : EnterParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after[15m1s] $exit)
            then
                System.out.printf("Re-enter more than 15 minutes after exiting [vehicle: %s, enter1: %s, exit: %s, enter2: %s]%n",
                    $enter1.vehicle().licensePlate(),
                    $enter1.timestamp(),
                    $exit.timestamp(),
                    $enter2.timestamp());
                delete($enter1);
                delete($exit);
            end

            rule "Issue invoice"
            when
                $enter : EnterParkingLotEvent()
                $exit : ExitParkingLotEvent(vehicle.licensePlate() == $enter.vehicle().licensePlate(), this after[2h1m] $enter)
            then
                System.out.printf("Parking duration more than 2 hours [vehicle: %s, enter: %s, exit: %s]%n",
                    $enter.vehicle().licensePlate(),
                    $enter.timestamp(),
                    $exit.timestamp());
                insert(new Invoice($exit.timestamp(), $enter.vehicle(), (int)Math.ceil(($exit.diffInMinutes($enter) - 120) / 60.0) * 3));
            end""";
}
