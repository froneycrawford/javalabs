// need to fix energy stuff

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


class RunTour extends Canvas implements MouseListener,
                    MouseMotionListener, Runnable
{
    /*  define Class variables
    *******************************************/
    final int WIDTH, HEIGHT;
    final int SIZE = 500;
    final double maxOrbitSize = 35.0;
    final double toRad = Math.PI/180.0;

    boolean running;
    Thread crash_thread;

    TextField atextEJ, atextJS, atextSU, atextUN;
    TextField etextEJ, etextJS, etextSU, etextUN;
    TextField ptextJ, ptextS, ptextU, ptextN;

    Graphics g;
    Image sunImage, earthImage, jupiterImage, saturnImage, uranusImage, neptuneImage;
    Image jsuccessImage,ssuccessImage, usuccessImage, nsuccessImage, successImage;
    Image failureImage, crashImage;
    Image offImage;

    final int capacity = 100000;
    int[] xCurr, yCurr;
    int lineNumber = 0;
    int earthx,earthy,jupiterx,jupitery,saturnx,saturny,uranusx,uranusy,neptunex,neptuney,
        satx,saty;

    double timeHold = -0.05;
    double time = 0.0;
    double energyUsed,arrivalEnergy,launchEnergy;
    double energyFac = 10000000.0;

    boolean seeVoyager = false;
    boolean success, failure;
    boolean goodLaunch = true;

    final double aEarth = 1.0;
    final double aJupiter = 5.2;
    final double aSaturn = 9.5;
    final double aUranus = 19.2;
    final double aNeptune = 30.1;

    boolean beenToJupiter, beenToSaturn, beenToUranus, beenToNeptune;
    double aLastPlanet, aNextPlanet;
    double xDiffPlanet, yDiffPlanet, rSun, rPlanet, rPlanetCrit;
    double tLastEnc, tDueIn;
    double[] earthPos, jupiterPos, saturnPos, uranusPos, neptunePos, satPos, planetPos;
    String planetName;

/*  these are solutions for Hohmann orbits
    double aSatJupiter = 3.1;
    double eSatJupiter = 0.67;
    double aSatSaturn = 7.35;
    double eSatSaturn = 0.29;
    double aSatUranus = 14.35;
    double eSatUranus = 0.34;
    double aSatNeptune = 24.65;
    double eSatNeptune = 0.22;
    double phiValJupiter = 82.87;
    double phiValSaturn = -23.94;
    double phiValUranus = 170.604;
    double phiValNeptune = 40.32;
*/

    double aSatJupiter = 1.0;
    double eSatJupiter = 0.0;
    double aSatSaturn = 0.0;
    double eSatSaturn = 0.0;
    double aSatUranus = 0.0;
    double eSatUranus = 0.0;
    double aSatNeptune = 0.0;
    double eSatNeptune = 0.0;
    double phiValJupiter = 0.0;
    double phiValSaturn = 0.0;
    double phiValUranus = 0.0;
    double phiValNeptune = 0.0;

    double phiRadJupiter, phiRadSaturn, phiRadUranus, phiRadNeptune;

    int aEarthScale = (int)(aEarth*(SIZE/(2.0*maxOrbitSize)));
    int aJupiterScale  = (int)(aJupiter*(SIZE/(2.0*maxOrbitSize)));
    int aSaturnScale  = (int)(aSaturn*(SIZE/(2.0*maxOrbitSize)));
    int aUranusScale  = (int)(aUranus*(SIZE/(2.0*maxOrbitSize)));
    int aNeptuneScale  = (int)(aNeptune*(SIZE/(2.0*maxOrbitSize)));

    Orbit earth = new Orbit(aEarth, 0.0);
    Orbit jupiter = new Orbit(aJupiter, 0.0);
    Orbit saturn = new Orbit(aSaturn, 0.0);
    Orbit uranus = new Orbit(aUranus, 0.0);
    Orbit neptune = new Orbit(aNeptune, 0.0);
    Orbit satellite = new Orbit(aSatJupiter, eSatJupiter);

    /*Constructor
    **********************************************************************/
    RunTour(int w, int h,
               TextField axisEJ, TextField axisJS, TextField axisSU, TextField axisUN,
               TextField eccEJ, TextField eccJS, TextField eccSU, TextField eccUN,
               TextField phiJ, TextField phiS, TextField phiU, TextField phiN,
               Image jsuccess, Image ssuccess, Image usuccess, Image nsuccess,
               Image f, Image e, Image j, Image s, Image u, Image n, Image sun)
    {
    WIDTH = w;
    HEIGHT = h;

    atextEJ = axisEJ;
    atextJS = axisJS;
    atextSU = axisSU;
    atextUN = axisUN;

    etextEJ = eccEJ;
    etextJS = eccJS;
    etextSU = eccSU;
    etextUN = eccUN;

    ptextJ = phiJ;
    ptextS = phiS;
    ptextU = phiU;
    ptextN = phiN;

    earthImage = e;
    jupiterImage = j;
    saturnImage = s;
    uranusImage = u;
    neptuneImage = n;
    sunImage = sun;

    jsuccessImage = jsuccess;
    ssuccessImage = ssuccess;
    usuccessImage = usuccess;
    nsuccessImage = nsuccess;

    failureImage = f;

    phiRadJupiter = toRad*phiValJupiter;
    phiRadSaturn = toRad*phiValSaturn;
    phiRadUranus = toRad*phiValUranus;
    phiRadNeptune = toRad*phiValNeptune;

    earthx = (int)(SIZE/2 + aEarthScale);
    earthy = (int)(SIZE/2);
    jupiterx = (int)(SIZE/2 - aJupiterScale * Math.cos(phiRadJupiter));
    jupitery = (int)(SIZE/2 + aJupiterScale * Math.sin(phiRadJupiter));
    saturnx = (int)(SIZE/2 - aSaturnScale * Math.cos(phiRadSaturn));
    saturny = (int)(SIZE/2 + aSaturnScale * Math.sin(phiRadSaturn));
    uranusx = (int)(SIZE/2 - aUranusScale * Math.cos(phiRadUranus));
    uranusy = (int)(SIZE/2 + aUranusScale * Math.sin(phiRadUranus));
    neptunex = (int)(SIZE/2 - aNeptuneScale * Math.cos(phiRadNeptune));
    neptuney = (int)(SIZE/2 + aNeptuneScale * Math.sin(phiRadNeptune));
    satx = SIZE/2 + aEarthScale;
    saty = SIZE/2;

    earthPos = new double[4];
    jupiterPos = new double[4];
    saturnPos = new double[4];
    uranusPos = new double[4];
    neptunePos = new double[4];
    satPos = new double[4];

    xCurr = new int[capacity];
    yCurr = new int[capacity];

    xCurr[0] = SIZE/2 + aEarthScale;
    yCurr[0] = SIZE/2;

    addMouseListener(this);
    addMouseMotionListener(this);
    crash_thread = new Thread(this);
    setFont(new Font("Times New Roman",Font.PLAIN,10));
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
    if(g == null)  {
        offImage = createImage(WIDTH, HEIGHT);
        g = offImage.getGraphics();
    }

    if(timeHold < 0.0)   {
        g.setFont(new Font("Roman", Font.BOLD, 12));
        g.setColor(Color.white);
        g.drawString("Applet Design:  Chris Mihos, Brian Lee", (int)(0.25*SIZE), (int)(SIZE/2));
        g.drawString("Click here to start", (int)(0.25*SIZE), (int)(SIZE/2 + 50));
        jupiter.tau = -365.25*Math.pow(jupiter.a,1.5)/(360.0/(180.-phiValJupiter));
        saturn.tau = -365.25*Math.pow(saturn.a,1.5)/(360.0/(180.-phiValSaturn));
        uranus.tau = -365.25*Math.pow(uranus.a,1.5)/(360.0/(180.-phiValUranus));
        neptune.tau = -365.25*Math.pow(neptune.a,1.5)/(360.0/(180.-phiValNeptune));
        planetName="Jupiter";
    } else {
        timeHold = 0.0;
        double years = time/365.25;

        /*  set text boxes to appropriate values
            *************************************************/
        atextEJ.setText(Double.toString(aSatJupiter));
        atextJS.setText(Double.toString(aSatSaturn));
        atextSU.setText(Double.toString(aSatUranus));
        atextUN.setText(Double.toString(aSatNeptune));

        etextEJ.setText(Double.toString(eSatJupiter));
        etextJS.setText(Double.toString(eSatSaturn));
        etextSU.setText(Double.toString(eSatUranus));
        etextUN.setText(Double.toString(eSatNeptune));

        ptextJ.setText(Double.toString(phiValJupiter));
        ptextS.setText(Double.toString(phiValSaturn));
        ptextU.setText(Double.toString(phiValUranus));
        ptextN.setText(Double.toString(phiValNeptune));

        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        /*  draw circles for planet orbits
         *************************************************/

        g.setColor(Color.white);
        g.drawOval(SIZE/2-aEarthScale, SIZE/2-aEarthScale, 2*aEarthScale, 2*aEarthScale);
        g.drawOval(SIZE/2-aJupiterScale, SIZE/2-aJupiterScale, 2*aJupiterScale, 2*aJupiterScale);
        g.drawOval(SIZE/2-aSaturnScale, SIZE/2-aSaturnScale, 2*aSaturnScale, 2*aSaturnScale);
        g.drawOval(SIZE/2-aUranusScale, SIZE/2-aUranusScale, 2*aUranusScale, 2*aUranusScale);
        g.drawOval(SIZE/2-aNeptuneScale, SIZE/2-aNeptuneScale, 2*aNeptuneScale, 2*aNeptuneScale);
        g.setFont(new Font("Roman", Font.BOLD, 12));
        g.drawString("Time:   "+Double.toString(roundN(years,2))+"  years",10,10);
        g.drawString("Probe-Sun Distance:   "+Double.toString(roundN(rSun,2))+" AU", 10,25);

        if(!beenToNeptune) {
            g.drawString("Probe-"+planetName+" Distance:   "+Double.toString(roundN(rPlanet,2))+
                         " AU",10, 40);
        }

        g.drawString("Energy Used:  "+Double.toString(roundN(energyUsed,2)), 10, 55);

        if(success) {

            g.setColor(Color.black);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.drawImage(successImage, (int)(0.5*(SIZE-successImage.getWidth(this))),
                                          (int)(0.5*(SIZE-successImage.getHeight(this))),this);

            g.setColor(Color.white);
            g.setFont(new Font("Roman", Font.BOLD, 22));
            g.drawString("Successful Encounter!", (int)(0.25*SIZE), (int)(SIZE/2));


            g.setFont(new Font("Roman", Font.BOLD, 12));
            g.drawString("Energy used for Encounter:  "+Double.toString(roundN(arrivalEnergy,2)),
                         (int)(0.25*SIZE), (int)(0.75*SIZE));

            if(!beenToNeptune) {
                g.drawString("Energy needed to Launch:   "+Double.toString(roundN(launchEnergy,2)),
                             (int)(0.25*SIZE), (int)(0.8*SIZE));
            } else {
                g.drawString("Total energy used:   "+Double.toString(roundN(energyUsed,2)),
                             (int)(0.25*SIZE), (int)(0.8*SIZE));
            }

            running=false;

        } else if(failure) {

            g.setColor(Color.black);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.white);
            g.setFont(new Font("Roman", Font.BOLD, 22));
            g.drawImage(failureImage, (int)(0.5*(SIZE-failureImage.getWidth(this))),
                                      (int)(0.5*(SIZE-failureImage.getHeight(this))),this);
            g.drawString("Failed Encounter", (int)(0.25*SIZE), (int)(SIZE/2));
            running=false;

        } else if(!success) {

            g.drawImage(earthImage, earthx-4, earthy-4, this);
            g.drawImage(jupiterImage, jupiterx-8, jupitery-8, this);
            g.drawImage(saturnImage, saturnx-18, saturny-8, this);
            g.drawImage(uranusImage, uranusx-8, uranusy-8, this);
            g.drawImage(neptuneImage, neptunex-8, neptuney-8, this);
            g.drawImage(sunImage, (SIZE/2-5), (SIZE/2-5), this);
            g.setColor(Color.green);
            for(int i=1; i < (lineNumber-1); i++) {
                g.drawLine(xCurr[i-1], yCurr[i-1], xCurr[i], yCurr[i]);
            }
            g.setColor(Color.red);
            g.fillOval(satx-2, saty-2, 5, 5);
        }

        if(!goodLaunch) {
            g.setFont(new Font("Roman", Font.BOLD, 16));
            g.setColor(Color.yellow);
            g.drawString("Orbital properties not correct for launch", (int)(0.25*SIZE), (int)(SIZE/2));
            running=false;
        }
    }

    h.drawImage(offImage, 0, 0, this);
    }

    /*  function run runs simulation
    *********************************************************************/
    public void run() {
    timeHold = 0.0;

    success = false;
    failure = false;

    while(running) {

        if (time == 0.0) {

        lineNumber = 1;
        satPos = satellite.orbitPosVel(0.0);
        planetPos = earth.orbitPosVel(0.0);

        launchEnergy = energyFac * 0.5*( (satPos[2]-planetPos[2])*(satPos[2]-planetPos[2]) +
                     (satPos[3]-planetPos[3])*(satPos[3]-planetPos[3]) );
        energyUsed=launchEnergy;

        }

        time += 7.0;

        earthPos = earth.orbitPosVel(time);
        jupiterPos = jupiter.orbitPosVel(time);
        saturnPos = saturn.orbitPosVel(time);
        uranusPos = uranus.orbitPosVel(time);
        neptunePos = neptune.orbitPosVel(time);

        if (!beenToJupiter) {
        planetName="Jupiter";
        rPlanetCrit=0.05;
        aNextPlanet=aJupiter;
        aLastPlanet=1.0;
        if (seeVoyager) {
            satellite.a=3.6298;
            satellite.e=0.724429;
            satellite.omega=toRad*11.703;
            satellite.M0=toRad*-0.888;
            satellite.Omega=toRad*-32.94;
            satellite.i=toRad*4.8257;
            satellite.tau=0.0;
        } else {
            satellite.a=aSatJupiter;
            satellite.e=eSatJupiter;
            satellite.omega=0.0;
            satellite.M0=0.0;
            satellite.Omega=0.0;
            satellite.tau=0.0;
            satellite.i=0.0;
            tLastEnc=0.0;
            tDueIn = 365.25*0.5*Math.pow(aSatJupiter,1.5);
        }
        }

        satPos = satellite.orbitPosVel(time);
        rSun = Math.sqrt(satPos[0]*satPos[0] + satPos[1]*satPos[1]);

        if ((rSun > 1.25*aNextPlanet) || ( (time-tLastEnc) > 1.25*tDueIn) ) {failure=true;}
        if (seeVoyager) {failure=false;}

        // BAD KLUDGE HERE TO FIX UN VOYAGER PROBLEM
        if (beenToUranus && ( rSun < aUranus ) ) {satPos=uranusPos;}

        if (!beenToJupiter) {
        planetPos=jupiterPos;
        } else if (!beenToSaturn) {
        planetPos=saturnPos;
        } else if (!beenToUranus) {
        planetPos=uranusPos;
        } else if (!beenToNeptune) {
        planetPos=neptunePos;
        }

        xDiffPlanet = planetPos[0] - satPos[0];
        yDiffPlanet = planetPos[1] - satPos[1];
        rPlanet = Math.sqrt(xDiffPlanet*xDiffPlanet + yDiffPlanet*yDiffPlanet);

        if (rPlanet < rPlanetCrit) {

        success = true;
        arrivalEnergy = energyFac * 0.5*( (satPos[2]-planetPos[2])*(satPos[2]-planetPos[2]) +
                      (satPos[3]-planetPos[3])*(satPos[3]-planetPos[3]) );
        if (!beenToJupiter) {
            beenToJupiter=true;
            successImage=jsuccessImage;
            planetName="Saturn";
            rPlanetCrit=0.05;
            aNextPlanet=aSaturn;
            aLastPlanet=aJupiter;
            if (seeVoyager) {
            satellite.a=14.8021;
            satellite.e=1.338264;
            satellite.omega=toRad*-9.171;
            satellite.M0=toRad*4.798;
            satellite.Omega=toRad*119.1969;
            satellite.tau=2444132.1718-2443379.1869;
            satellite.i=toRad*2.5823;
            } else {
            satellite.a=aSatSaturn;
            satellite.e=eSatSaturn;
            satellite.omega=toRad*180.0;
            satellite.M0=0.0;
            satellite.Omega=0.0;
            satellite.tau=time;
            satellite.i=0.0;
            tLastEnc=time;
            tDueIn = 365.25*0.5*Math.pow(aSatSaturn,1.5);
            }

        } else if (!beenToSaturn) {
            beenToSaturn=true;
            successImage=ssuccessImage;
            planetName="Uranus";
            rPlanetCrit=0.1;
            aNextPlanet=aUranus;
            aLastPlanet=aSaturn;
            if (seeVoyager) {
            satellite.a=3.8706;
            satellite.e=3.48023;
            satellite.omega=toRad*112.29;
            satellite.M0=toRad*10.351;
            satellite.Omega=toRad*76.8605;
            satellite.tau=2444895.4888-2443379.1869;
            satellite.i=toRad*2.665128;
            } else {
            satellite.a=aSatUranus;
            satellite.e=eSatUranus;
            satellite.omega=0.0;
            satellite.M0=0.0;
            satellite.Omega=0.0;
            satellite.tau=time;
            satellite.i=0.0;
            tLastEnc=time;
            tDueIn = 365.25*0.5*Math.pow(aSatUranus,1.5);
            }

        } else if (!beenToUranus) {
            beenToUranus=true;
            successImage=usuccessImage;
            planetName="Neptune";
            aNextPlanet=aNeptune;
            aLastPlanet=aUranus;
            rPlanetCrit=0.1;
            if (seeVoyager) {
            satellite.a=2.9877;
            satellite.e=5.8068;
            satellite.omega=toRad*-46.104;
            satellite.M0=toRad*315.0187;
            satellite.Omega=toRad*-100.376161;
            satellite.tau=2446955.7083-2443379.1869;
            satellite.i=toRad*2.4962;
            } else {
            satellite.a=aSatNeptune;
            satellite.e=eSatNeptune;
            satellite.omega=toRad*180.0;
            satellite.M0=0.0;
            satellite.Omega=0.0;
            satellite.tau=time;
            satellite.i=0.0;
            tLastEnc=time;
            tDueIn = 365.25*0.5*Math.pow(aSatNeptune,1.5);
            }

        } else if (!beenToNeptune) {
            beenToNeptune=true;
            successImage=nsuccessImage;
            aLastPlanet=aNeptune;
            if (seeVoyager) {
            satellite.a=4.007;
            satellite.e=6.2846;
            satellite.omega=toRad*130.044;
            satellite.M0=toRad*342.971;
            satellite.Omega=toRad*100.935;
            satellite.tau=2448257.7083-2443379.1869;
            satellite.i=toRad*78.8102;
            } else {

            }
        }

        satPos = satellite.orbitPosVel(time+0.0);
        System.out.println(satPos[0]+" "+satPos[1]);
        System.out.println(satPos[2]+" "+satPos[3]+" "+planetPos[2]+" "+planetPos[3]);


        if ( Math.abs(1 - satellite.a*(1-satellite.e)/aLastPlanet) > 0.05 &&
             !seeVoyager && !beenToNeptune) {goodLaunch=false;}

        launchEnergy = energyFac * 0.5*( (satPos[2]-planetPos[2])*(satPos[2]-planetPos[2]) +
                         (satPos[3]-planetPos[3])*(satPos[3]-planetPos[3]) );

        energyUsed = energyUsed + arrivalEnergy + launchEnergy;

        } // if rPlanet

        earthx = (int)((earthPos[0]+maxOrbitSize)*(SIZE/(2.0*maxOrbitSize)));
        earthy = (int)((earthPos[1]+maxOrbitSize)*(SIZE/(2.0*maxOrbitSize)));
        jupiterx = (int)((jupiterPos[0]+maxOrbitSize)*(SIZE/(2.0*maxOrbitSize)));
        jupitery = (int)((jupiterPos[1]+maxOrbitSize)*(SIZE/(2.0*maxOrbitSize)));
        saturnx = (int)((saturnPos[0]+maxOrbitSize)*(SIZE/(2.0*maxOrbitSize)));
        saturny = (int)((saturnPos[1]+maxOrbitSize)*(SIZE/(2.0*maxOrbitSize)));
        uranusx = (int)((uranusPos[0]+maxOrbitSize)*(SIZE/(2.0*maxOrbitSize)));
        uranusy = (int)((uranusPos[1]+maxOrbitSize)*(SIZE/(2.0*maxOrbitSize)));
        neptunex = (int)((neptunePos[0]+maxOrbitSize)*(SIZE/(2.0*maxOrbitSize)));
        neptuney = (int)((neptunePos[1]+maxOrbitSize)*(SIZE/(2.0*maxOrbitSize)));

        satx = (int)((satPos[0]+maxOrbitSize)*(SIZE/(2.0*maxOrbitSize)));
        saty = (int)((satPos[1]+maxOrbitSize)*(SIZE/(2.0*maxOrbitSize)));

        xCurr[lineNumber]=satx;
        yCurr[lineNumber]=saty;
        lineNumber += 1;

        repaint();

        try {
        crash_thread.sleep(10);
        }
        catch(InterruptedException e){
        System.out.println(e.toString());
        }

    }

    }

    /*  function go implements new thread if boolean running is true
    ***************************************************************************/
    public void go(boolean b) {
    if(goodLaunch) {
        running = b;

        if(running) {
        crash_thread = new Thread(this);
        crash_thread.start();
        }
    }
    }

    /*  function paramSwitch implements new values input to textfields
    ***************************************************************************/
    public void paramSwitch() {

    timeHold = 0.0;

    String holderAJupiter = atextEJ.getText();
    aSatJupiter = Double.valueOf(holderAJupiter).doubleValue();
    atextEJ.setText(Double.toString(roundN(aSatJupiter,2)));

    String holderEJupiter = etextEJ.getText();
    eSatJupiter = Double.valueOf(holderEJupiter).doubleValue();
    etextEJ.setText(Double.toString(roundN(eSatJupiter,2)));

    String holderASaturn = atextJS.getText();
    aSatSaturn = Double.valueOf(holderASaturn).doubleValue();
    atextJS.setText(Double.toString(roundN(aSatSaturn,2)));

    String holderESaturn = etextJS.getText();
    eSatSaturn = Double.valueOf(holderESaturn).doubleValue();
    etextJS.setText(Double.toString(roundN(eSatSaturn,2)));

    String holderAUranus = atextSU.getText();
    aSatUranus = Double.valueOf(holderAUranus).doubleValue();
    atextSU.setText(Double.toString(roundN(aSatUranus,2)));

    String holderEUranus = etextSU.getText();
    eSatUranus = Double.valueOf(holderEUranus).doubleValue();
    etextSU.setText(Double.toString(roundN(eSatUranus,2)));

    String holderANeptune = atextUN.getText();
    aSatNeptune = Double.valueOf(holderANeptune).doubleValue();
    atextUN.setText(Double.toString(roundN(aSatNeptune,2)));

    String holderENeptune = etextUN.getText();
    eSatNeptune = Double.valueOf(holderENeptune).doubleValue();
    etextUN.setText(Double.toString(roundN(eSatNeptune,2)));

    String holderPhiJupiter = ptextJ.getText();
    phiValJupiter = Double.valueOf(holderPhiJupiter).doubleValue();
    ptextJ.setText(Double.toString(roundN(phiValJupiter,1)));
    phiRadJupiter = phiValJupiter*toRad;

    String holderPhiSaturn = ptextS.getText();
    phiValSaturn = Double.valueOf(holderPhiSaturn).doubleValue();
    ptextS.setText(Double.toString(roundN(phiValSaturn,1)));
    phiRadSaturn = phiValSaturn*toRad;

    String holderPhiUranus = ptextU.getText();
    phiValUranus = Double.valueOf(holderPhiUranus).doubleValue();
    ptextU.setText(Double.toString(roundN(phiValUranus,1)));
    phiRadUranus = phiValUranus*toRad;

    String holderPhiNeptune = ptextN.getText();
    phiValNeptune = Double.valueOf(holderPhiNeptune).doubleValue();
    ptextN.setText(Double.toString(roundN(phiValNeptune,1)));
    phiRadNeptune = phiValNeptune*toRad;

    jupiter.tau = -365.25*Math.pow(jupiter.a,1.5)/(360.0/(180.-phiValJupiter));
    saturn.tau = -365.25*Math.pow(saturn.a,1.5)/(360.0/(180.-phiValSaturn));
    uranus.tau = -365.25*Math.pow(uranus.a,1.5)/(360.0/(180.-phiValUranus));
    neptune.tau = -365.25*Math.pow(neptune.a,1.5)/(360.0/(180.-phiValNeptune));

    reset();
    repaint();
    }

    /*  function roundN rounds a double to N decimal place
    ***************************************************************************/
    public double roundN(double r, int N)
    {
    r *= Math.pow(10.,N);
    r = Math.round(r);
    r /= Math.pow(10.,N);
    return r;
    }

    /*  function reset sets simulation back to beginning
    ***************************************************************************/
    public void reset()
    {
    earthx = SIZE/2 + aEarthScale;
    earthy = SIZE/2;
    jupiterx = (int)(SIZE/2 - aJupiterScale * Math.cos(phiRadJupiter));
    jupitery = (int)(SIZE/2 + aJupiterScale * Math.sin(phiRadJupiter));
    saturnx = (int)(SIZE/2 - aSaturnScale * Math.cos(phiRadSaturn));
    saturny = (int)(SIZE/2 + aSaturnScale * Math.sin(phiRadSaturn));
    uranusx = (int)(SIZE/2 - aUranusScale * Math.cos(phiRadUranus));
    uranusy = (int)(SIZE/2 + aUranusScale * Math.sin(phiRadUranus));
    neptunex = (int)(SIZE/2 - aNeptuneScale * Math.cos(phiRadNeptune));
    neptuney = (int)(SIZE/2 + aNeptuneScale * Math.sin(phiRadNeptune));

    satx = SIZE/2 + aEarthScale;
    saty = SIZE/2;

    jupiter.tau = -365.25*Math.pow(jupiter.a,1.5)/(360.0/(180.-phiValJupiter));
    saturn.tau  = -365.25*Math.pow(saturn.a,1.5)/(360.0/(180.-phiValSaturn));
    uranus.tau  = -365.25*Math.pow(uranus.a,1.5)/(360.0/(180.-phiValUranus));
    neptune.tau = -365.25*Math.pow(neptune.a,1.5)/(360.0/(180.-phiValNeptune));


    time = 0.0;
    lineNumber = 0;
    tLastEnc=0.0;

    rSun = 0.0;
    rPlanet = 0.0;

    success = false;
    failure = false;
    beenToJupiter = false;
    beenToSaturn = false;
    beenToUranus = false;
    beenToNeptune = false;

    energyUsed = 0.0;

    goodLaunch=true;

    planetName="Jupiter";

    repaint();
    }

    /* function setVoy toggles Voyager orbit on/off
    ***********************************************/
    public void setVoy(boolean b) {

    seeVoyager=b;

    if (seeVoyager) {
        phiValJupiter = 98.4;
        phiValSaturn = 38.0;
        phiValUranus = -42.5;
        phiValNeptune = -76.5;
        aSatJupiter = 3.63;
        eSatJupiter = 0.72;
        aSatSaturn = 14.80;
        eSatSaturn = 1.34;
        aSatUranus = 3.87;
        eSatUranus = 3.48;
        aSatNeptune = 2.99;
        eSatNeptune = 5.81;
    } else {
        phiValJupiter = 0.0;
        phiValSaturn = 0.0;
        phiValUranus = 0.0;
        phiValNeptune = 0.0;
        aSatJupiter = 1.0;
        eSatJupiter = 0.0;
        aSatSaturn = 0.0;
        eSatSaturn = 0.0;
        aSatUranus = 0.0;
        eSatUranus = 0.0;
        aSatNeptune = 0.0;
        eSatNeptune = 0.0;
    }

    phiRadJupiter = phiValJupiter*toRad;
    phiRadSaturn = phiValSaturn*toRad;
    phiRadUranus = phiValUranus*toRad;
    phiRadNeptune = phiValNeptune*toRad;
    ptextJ.setText(Double.toString(roundN(phiValJupiter,1)));
    ptextS.setText(Double.toString(roundN(phiValSaturn,1)));
    ptextU.setText(Double.toString(roundN(phiValUranus,1)));
    ptextN.setText(Double.toString(roundN(phiValNeptune,1)));
    atextEJ.setText(Double.toString(roundN(aSatJupiter,2)));
    etextEJ.setText(Double.toString(roundN(eSatJupiter,2)));
    atextJS.setText(Double.toString(roundN(aSatSaturn,2)));
    etextJS.setText(Double.toString(roundN(eSatSaturn,2)));
    atextSU.setText(Double.toString(roundN(aSatUranus,2)));
    etextSU.setText(Double.toString(roundN(eSatUranus,2)));
    atextUN.setText(Double.toString(roundN(aSatNeptune,2)));
    etextUN.setText(Double.toString(roundN(eSatNeptune,2)));

    reset();
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

