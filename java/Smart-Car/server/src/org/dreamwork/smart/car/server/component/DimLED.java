package org.dreamwork.smart.car.server.component;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;
import org.apache.log4j.Logger;
import org.dreamwork.smart.car.server.util.PausableThread;

/**
 * Created by Meng on 2015/8/8.
 */
public class DimLED extends PausableThread {
    private static final Logger logger = Logger.getLogger (DimLED.class);

    private int pin_number = 1;

    public DimLED (int pinIndex) {
        super (true);
        // initialize wiringPi library, this is needed for PWM
        Gpio.wiringPiSetup();
        // softPwmCreate(int pin, int value, int range)
        // the range is set like (min=0 ; max=100)
        pin_number = pinIndex;
        SoftPwm.softPwmCreate(pin_number, 0, 100);
        logger.debug ("dim led standby on " + pin_number + "!");
    }

    @Override
    protected void doWork () {
        try {
            // fade LED to fully ON
            for (int i = 0; i <= 100; i++) {
                // softPwmWrite(int pin, int value)
                // This updates the PWM value on the given pin. The value is
                // checked to be in-range and pins
                // that haven't previously been initialized via softPwmCreate
                // will be silently ignored.
                SoftPwm.softPwmWrite(pin_number, i);
                Thread.sleep(25);
            }
            // fade LED to fully OFF
            for (int i = 100; i >= 0; i--) {
                SoftPwm.softPwmWrite(pin_number, i);
                Thread.sleep(25);
            }
        } catch (Exception ex) {
            logger.warn (ex.getMessage (), ex);
        }
    }
}
