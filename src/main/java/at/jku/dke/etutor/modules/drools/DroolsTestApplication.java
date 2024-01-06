package at.jku.dke.etutor.modules.drools;

import at.jku.dke.etutor.modules.drools.analysis.DroolsAnalysis;

import java.io.IOException;

public class DroolsTestApplication {

    public static void main(String[] args) throws IOException, ReflectiveOperationException {

        DroolsAnalysis analysis = new DroolsAnalysis(1, RULES, true);
        analysis.analyze();

//        analysis.hasSyntaxError();
//

//        analysis.createSampleSolution(true);
//        analysis.createSampleSolution(false);

        long additionalFacts = analysis.getAdditionalFactInformation().values().stream()
                .mapToLong(Long::longValue)
                .sum();

        if (additionalFacts > 0) System.out.println("Es sind um " + additionalFacts + " Fakten zu viel.");
        if (additionalFacts < 0) System.out.println("Es sind um " + additionalFacts + " Fakten zu wenig.");

        int wrongFacts = analysis.getWrongFactList().size();
        if (wrongFacts == 0) System.out.println("Regeln sind korrekt");
        else System.out.println("Es sind " + wrongFacts + " Fakten falsch.");

        System.out.println(analysis);

    }

    private static final String JSON = "[{\"class\": \"at.jku.dke.etutor.modules.drools.jit.EnterParkingLotEvent\", \"vehicle\": {\"class\": \"at.jku.dke.etutor.modules.drools.jit.Vehicle\", \"licensePlate\": \"W-444D\"}, \"timestamp\": \"Sun Jan 01 11:00:00 CET 2023\"}, {\"class\": \"at.jku.dke.etutor.modules.drools.jit.EnterParkingLotEvent\", \"vehicle\": {\"class\": \"at.jku.dke.etutor.modules.drools.jit.Vehicle\", \"licensePlate\": \"S-333D\"}, \"timestamp\": \"Sun Jan 01 06:00:00 CET 2023\"}, {\"class\": \"at.jku.dke.etutor.modules.drools.jit.EnterParkingLotEvent\", \"vehicle\": {\"class\": \"at.jku.dke.etutor.modules.drools.jit.Vehicle\", \"licensePlate\": \"L-888D\"}, \"timestamp\": \"Wed Feb 01 14:00:00 CET 2023\"}, {\"class\": \"at.jku.dke.etutor.modules.drools.jit.EnterParkingLotEvent\", \"vehicle\": {\"class\": \"at.jku.dke.etutor.modules.drools.jit.Vehicle\", \"licensePlate\": \"L-111D\"}, \"timestamp\": \"Sun Jan 01 11:00:00 CET 2023\"}, {\"class\": \"at.jku.dke.etutor.modules.drools.jit.ExitParkingLotEvent\", \"vehicle\": {\"class\": \"at.jku.dke.etutor.modules.drools.jit.Vehicle\", \"licensePlate\": \"L-111D\"}, \"timestamp\": \"Sun Jan 01 13:30:00 CET 2023\"}, {\"class\": \"at.jku.dke.etutor.modules.drools.jit.ExitParkingLotEvent\", \"vehicle\": {\"class\": \"at.jku.dke.etutor.modules.drools.jit.Vehicle\", \"licensePlate\": \"S-333D\"}, \"timestamp\": \"Sun Jan 01 07:30:00 CET 2023\"}, {\"class\": \"at.jku.dke.etutor.modules.drools.jit.ExitParkingLotEvent\", \"vehicle\": {\"class\": \"at.jku.dke.etutor.modules.drools.jit.Vehicle\", \"licensePlate\": \"W-444D\"}, \"timestamp\": \"Sun Jan 01 16:15:00 CET 2023\"}, {\"class\": \"at.jku.dke.etutor.modules.drools.jit.ExitParkingLotEvent\", \"vehicle\": {\"class\": \"at.jku.dke.etutor.modules.drools.jit.Vehicle\", \"licensePlate\": \"L-888D\"}, \"timestamp\": \"Wed Feb 01 14:30:00 CET 2023\"}, {\"class\": \"at.jku.dke.etutor.modules.drools.jit.Invoice\", \"price\": 3, \"vehicle\": {\"class\": \"at.jku.dke.etutor.modules.drools.jit.Vehicle\", \"licensePlate\": \"L-111D\"}, \"invoiceDate\": \"Sun Jan 01 13:30:00 CET 2023\"}, {\"class\": \"at.jku.dke.etutor.modules.drools.jit.Invoice\", \"price\": 3, \"vehicle\": {\"class\": \"at.jku.dke.etutor.modules.drools.jit.Vehicle\", \"licensePlate\": \"L-888D\"}, \"invoiceDate\": \"Wed Feb 01 13:40:00 CET 2023\"}, {\"class\": \"at.jku.dke.etutor.modules.drools.jit.Invoice\", \"price\": 12, \"vehicle\": {\"class\": \"at.jku.dke.etutor.modules.drools.jit.Vehicle\", \"licensePlate\": \"W-444D\"}, \"invoiceDate\": \"Sun Jan 01 16:15:00 CET 2023\"}]";

    private static final String RULES = """
            package at.jku.dke.etutor.modules.drools.jit;
                        
            rule "Combine parking intervals if reentry within 15 min"
            when
                $enter1 : EnterParkingLotEvent()
                $exit : ExitParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after $enter1)
                $enter2 : EnterParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after[0s, 50m] $exit)
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
