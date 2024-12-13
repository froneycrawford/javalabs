import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.Color.*;
import java.lang.reflect.*;
import java.lang.Object.*;
import java.lang.Math.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.math.*;
import java.applet.Applet;

class RunSimulation extends Canvas implements MouseListener, MouseMotionListener, Runnable
{
    /*  define Class variables
    *******************************************/
    Graphics g;
    double timeHold = -0.05;
    double time = 0.0;
    int lineNumber = 0;
    int prevLine = 0;

    Image earthImage, marsImage, sunImage;
    Image offImage;
    Image successImage, failureImage, crashImage;

    TextField textA, textE, textPhi;

    boolean running;
    Thread crash_thread;

    final int WIDTH;
    final int HEIGHT;
    int SIZE = 500;
    final double maxOrbitSize = 2.0;
    boolean aeValid = true;
    boolean success = false;
    boolean failure = false;
    boolean crash = false;

    int point_index = 0;

    double aEarth = 1.0;
    double eEarth = 0.0;
    double aMars = 1.524;
    double eMars = 0.0;
    double aSatellite = 1.25;
    double eSatellite = 0.2;
    double phiVal, phiRad, diffx, diffy, r;

    int aEarthScale = (int)(aEarth*(SIZE/(2.0*maxOrbitSize)));
    int aMarsScale  = (int)(aMars*(SIZE/(2.0*maxOrbitSize)));

    Orbit earth = new Orbit(aEarth, eEarth);
    Orbit mars = new Orbit(aMars, eMars, phiRad);
    Orbit satellite = new Orbit(aSatellite, eSatellite);

    final int capacity = 100000;

    int earthx = SIZE/2 + aEarthScale;
    int earthy = SIZE/2;
    int marsx = (int)(SIZE/2 - aMarsScale * Math.cos(phiRad));
    int marsy = (int)(SIZE/2 + aMarsScale * Math.sin(phiRad));
    int satx = SIZE/2 + aEarthScale;
    int saty = SIZE/2;

    int[] currx, curry;

    double[] earthPos, marsPos, satPos;

    /*  Constructor
    ********************************************************************************************************/
    RunSimulation(int w, int h, TextField semimajor, TextField eccentricity, TextField phi,
                  Image e, Image m, Image s, Image satellite, Image success, Image f, Image c)
    {
        WIDTH = w;
        HEIGHT = h;

        textA = semimajor;
        textE = eccentricity;
        textPhi = phi;

        earthImage = e;
        marsImage = m;
        sunImage = s;
        successImage = success;
        failureImage = f;
        crashImage = c;

        earthPos = new double[2];
        marsPos = new double[2];
        satPos = new double[2];

        currx = new int[capacity];
        curry = new int[capacity];
        currx[0] = SIZE/2 + aEarthScale;
        curry[0] = SIZE/2;

        addMouseListener(this);
        addMouseMotionListener(this);
        crash_thread = new Thread(this);

        setBackground(Color.black);
    }

    /*  function paint calls update
    *****************************************************/
    public void paint(Graphics g)
    {
        update(g);
    }

    /*  function update loads and defines graphics
    *****************************************************/
    public void update(Graphics h)
    {
        if(g == null)
        {
            offImage = createImage(500, 630);
            g = offImage.getGraphics();
        }

        if(timeHold < 0.0)
        {
            g.setFont(new Font("Roman", Font.BOLD, 12));
            g.setColor(Color.white);
            g.drawString("Applet Design:  Chris Mihos, Brian Lee", 150, 250);
            g.drawLine(0, 515, 500, 515);
        }
        else
        {
            timeHold = 0.0;
            double years = time/365.0;

            /*  erase canvas with movement of the mouse
            *************************************************/

            g.setColor(Color.black);
            g.fillRect(0, 0, 500, 630);

            g.setColor(Color.white);
            g.drawLine(0, 515, 500, 515);

            /*  set text boxes to appropriate values
            *************************************************/
            textA.setText(Double.toString(aSatellite));
            textE.setText(Double.toString(eSatellite));
            textPhi.setText(Double.toString(phiVal));

            /*  draw circles for earth and mars orbits
            *************************************************/
            g.setColor(Color.white);
            g.drawOval(SIZE/2-aEarthScale, SIZE/2-aEarthScale, 2*aEarthScale, 2*aEarthScale);
            g.drawOval(SIZE/2-aMarsScale, SIZE/2-aMarsScale, 2*aMarsScale, 2*aMarsScale);

            if(aeValid)
            {
                g.setFont(new Font("Roman", Font.BOLD, 12));

                g.drawString("Time: "+Double.toString(roundTwo(years))+" years",10,15);
                g.drawString("Probe-Mars Distance: "+Double.toString(roundTwo(r))+" AU",10,30);

                if(success)
                {
                    g.setColor(Color.black);
                    g.fillRect(0, 0, 500, 630);
                    g.setColor(Color.white);
                    g.setFont(new Font("Roman", Font.BOLD, 22));
                    g.drawString("Successful landing!", 150, 70);
                    g.drawImage(successImage, 120, 165, this);
                }
                else if(failure)
                {
                    g.setColor(Color.black);
                    g.fillRect(0, 0, 500, 630);
                    g.setColor(Color.white);
                    g.setFont(new Font("Roman", Font.BOLD, 22));
                    g.drawString("Failed Mars Landing", 150, 70);
                    g.drawImage(failureImage, 125, 155, this);
                }
                else if(crash)
                {
                    g.setColor(Color.black);
                    g.fillRect(0, 0, 500, 630);
                    g.setColor(Color.white);
                    g.setFont(new Font("Roman", Font.BOLD, 22));
                    g.drawString("High speed crash!", 155, 70);
                    g.drawImage(crashImage, 110, 155, this);
                }
                else if(!success)
                {
                    /*  draws images of earth, mars, sun, and satellite
                    ****************************************************/
                    g.setColor(Color.green);
                    for(int i=1; i < lineNumber-1; i++)
                    {
                        g.drawLine(currx[i-1], curry[i-1], currx[i], curry[i]);
                    }
                    g.setColor(Color.red);
                    g.drawImage(earthImage, earthx-8, earthy-8, this);
                    g.drawImage(marsImage, marsx-8, marsy-8, this);
                    g.fillOval(satx-5, saty-5, 10, 10);
                    g.drawImage(sunImage, (SIZE/2-15), (SIZE/2-15), this);
                }
            }
            else if(!aeValid)
            {
                g.setFont(new Font("Roman", Font.BOLD, 12));
                g.setColor(Color.yellow);
                g.drawString("A, E invalid - probe needs to launch from Earth!", 130, 40);
            }
        }

        h.drawImage(offImage, 0, 0, this);
    }

    /*  function run detects mouse movement
    *********************************************************************/
    public void run()
    {
        timeHold = 0.0;

        while(running)
        {
            time += 1.0;

            earthPos = earth.orbitPosition(time/365.0);
            marsPos = mars.orbitPosition(time/365.0);
            satPos = satellite.orbitPosition(time/365.0);

            diffx = marsPos[0]-satPos[0];
            diffy = marsPos[1]-satPos[1];
            r = Math.sqrt(diffx*diffx + diffy*diffy);

            earthx = (int)((earthPos[0]+maxOrbitSize)*(SIZE/(2.0*maxOrbitSize)));
            earthy = (int)((earthPos[1]+maxOrbitSize)*(SIZE/(2.0*maxOrbitSize)));
            marsx = (int)((marsPos[0]+maxOrbitSize)*(SIZE/(2.0*maxOrbitSize)));
            marsy = (int)((marsPos[1]+maxOrbitSize)*(SIZE/(2.0*maxOrbitSize)));
            satx = (int)((satPos[0]+maxOrbitSize)*(SIZE/(2.0*maxOrbitSize)));
            saty = (int)((satPos[1]+maxOrbitSize)*(SIZE/(2.0*maxOrbitSize)));

            if(r <= 0.01 && time > 365.0*(.47*satellite.period) && time < 365.0*(.53*satellite.period))
            {
                running = false;
                success = true;
            }

            if(time > 365.0*(0.6*satellite.period) || satx < 0 || saty > 520)
            {
                running = false;
                failure = true;
            }

            if(r <= 0.01 && (time < 365.0*(.47*satellite.period) || time > 365.0*(.53*satellite.period)))
            {
                running = false;
                crash = true;
            }

            currx[lineNumber]=satx;
            curry[lineNumber]=saty;
            lineNumber += 1;

            repaint();

            try
            {
                crash_thread.sleep(10);
            }
            catch(InterruptedException e)
            {
                System.out.println(e.toString());
            }
        }
    }

    /*  function go implements new thread if boolean running is true
    ***************************************************************************/
    public void go(boolean b)
    {
        if(aeValid)
        {
            running = b;

            if(running)
            {
                crash_thread = new Thread(this);
                crash_thread.start();
            }
        }
    }

    /*  function paramSwitch implements new scale values input to textfields
    ***************************************************************************/
    public void paramSwitch()
    {
        timeHold = 0.0;

        String holderA = textA.getText();
        aSatellite = Double.valueOf(holderA).doubleValue();
        textA.setText(Double.toString(round(aSatellite)));

        String holderE = textE.getText();
        eSatellite = Double.valueOf(holderE).doubleValue();
        textE.setText(Double.toString(round(eSatellite)));

        if(aSatellite*(1.0-eSatellite) >= .99 && aSatellite*(1.0-eSatellite) < 1.1)
        {
            aeValid = true;
        }
        else
        {
            aeValid = false;
        }

        String holderPhi = textPhi.getText();
        phiVal = Double.valueOf(holderPhi).doubleValue();
        textPhi.setText(Double.toString(round(phiVal)));

        phiRad = phiVal/180.0 * Math.PI;

        satellite.updateOrbit(aSatellite, eSatellite, 0.0);
        mars.updateOrbit(aMars, eMars, (180.-phiVal));

        reset();
        repaint();
    }

    /*  function round rounds a double to one decimal place
    ***************************************************************************/
    public double round(double r)
    {
        r *= 10;
        r = Math.round(r);
        r /= 10;
        return r;
    }

    /*  function roundTwo rounds a double to two decimal places
    ***************************************************************************/
    public double roundTwo(double r)
    {
        r *= 100;
        r = Math.round(r);
        r /= 100;
        return r;
    }
    /*  function reset sets earth and mars back to original positions
    ***************************************************************************/
    public void reset()
    {
        earthx = SIZE/2 + aEarthScale;
        earthy = SIZE/2;
        marsx = (int)(SIZE/2 - aMarsScale * Math.cos(phiRad));
        marsy = (int)(SIZE/2 + aMarsScale * Math.sin(phiRad));
        satx = SIZE/2 + aEarthScale;
        saty = SIZE/2;

        time = 0.0;
        lineNumber = 0;
        r=0.;

        success = false;
        failure = false;
        crash = false;

        repaint();
    }

    /*  functions for mouse actions
    ******************************************************************************************/
    public void mouseClicked(MouseEvent e) {timeHold = 0.0; repaint();}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {};
    public void mouseReleased(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
}

