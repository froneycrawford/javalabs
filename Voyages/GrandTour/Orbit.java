public class Orbit
{
    double a, e, tau, omega, Omega, i, M0, M;
    double G = 0.0002959218;

    boolean swap = false;

    /*  Constructors for Orbit
    **************************************************/
    Orbit(double ain, double ein, double phiin)
    {
        double phi=phiin;

        tau = -Math.pow(a,1.5) * phi / 360.0;
        a = ain;
        e = ein;
        omega=0.0;
        Omega=0.0;
        i=0.0;
        M0=(2.*Math.PI/360.)*phiin;
    }

    Orbit(double ain, double ein, double tauin, double omegain,
          double Omegain, double iin, double M0in)
    {
        a=ain;
        e=ein;
        tau=tauin;
        omega=(2.*Math.PI/360.)*omegain;
        Omega=(2.*Math.PI/360.)*Omegain;
        i=(2.*Math.PI/360.)*iin;
        M0=(2.*Math.PI/360.)*omegain;
    }

    Orbit(double ain, double ein)
    {
        a = ain;
        e = ein;
        tau = 0.0;
    }

    Orbit()
    {
        a = 1.0;
        e = 0.0;
        tau = 0.0;
    }

    /*  function orbitPosition
    ******************************************************/
    public double[] orbitPosVel(double time)
    {
        double[] posvel = new double[4];
        double bigE, r, tanfon2, f, nu;
        double bigFi, bigF, Mcrit, Y, sinh3w, w;
        double ugh1,ugh2,ugh3,ughfac;

        nu=Math.sqrt(G/a/a/a);
        M=nu*(time-tau) + M0;
        bigE=0.0;

        if (e < 1) {
            bigE = solveKepler(M);
            r = a * (1-e * Math.cos(bigE));
            tanfon2 = Math.sqrt((1+e)/(1-e)) * Math.tan(bigE/2.0);
            f = 2.0 * Math.atan(tanfon2);
        } else {
            Mcrit=5.*e-2.5;
            if (M>Mcrit) {
                bigFi=Math.log(2.*M/e);
            } else {
                Y=Math.sqrt(8.*(e-1)/e);
                sinh3w=3.*M/(Y*(e-1));
                w=(1./3.)*Math.log(sinh3w+Math.sqrt(sinh3w*sinh3w+1));
                bigFi=Y*this.sinh(w);
            }
            bigF = solveHypKepler((bigFi-5.0),(bigFi+5.0));
            tanfon2 = Math.sqrt((e+1)/(e-1)) * this.tanh(bigF/2.0);
            f = 2.0 * Math.atan(tanfon2);
            r=a*(e*e-1.)/(1+e*Math.cos(f));
        }

        ugh1=r*(Math.cos(f+omega)*Math.cos(Omega)-Math.sin(f+omega)*Math.sin(Omega)*Math.cos(i));
        ugh2=r*(Math.cos(f+omega)*Math.sin(Omega)+Math.sin(f+omega)*Math.cos(Omega)*Math.cos(i));
        ugh3=r*Math.sin(f+omega)*Math.sin(i);

        posvel[0] = ugh1;
        if ( i != 0) {
            posvel[1] = ugh2*Math.cos(0.41015)-ugh3*Math.sin(0.41015);
        } else {
            posvel[1] = ugh2;
        }

        if (e<1) {
            ughfac=nu*a/Math.sqrt(1-e*e);
        } else {
            ughfac=nu*a/Math.sqrt(e*e-1);
        }
        ugh1=-( (Math.sin(f+omega)+e*Math.sin(omega))*Math.cos(Omega) +
                (Math.cos(f+omega)+e*Math.cos(omega))*Math.sin(Omega)*Math.cos(i) );

        ugh2=-( (Math.sin(f+omega)+e*Math.sin(omega))*Math.sin(Omega) -
                (Math.cos(f+omega)+e*Math.cos(omega))*Math.cos(Omega)*Math.cos(i) );

        ugh3=  (Math.cos(f+omega)+e*Math.cos(omega))*Math.sin(i);

        ugh1=ugh1*ughfac;
        ugh2=ugh2*ughfac;
        ugh3=ugh3*ughfac;

        posvel[2] = ugh1;
        if ( i != 0) {
            posvel[3] = ugh2*Math.cos(0.41015)-ugh3*Math.sin(0.41015);
        } else {
            posvel[3] = ugh2;
        }

        return posvel;
    }


    /*  function solveKepler
    ******************************************************/
    public double solveKepler(double M)
    {
        double diff = 1.0;
        double bigE, bigEnew;

        bigE = M;

        while(diff > 0.0001)
        {
            bigEnew = bigE + (M-bigE+e * Math.sin(bigE))/(1-e * Math.cos(bigE));
            diff = Math.abs((bigEnew-bigE)/bigE);
            bigE = bigEnew;
        }
        return bigE;
    }

    /*  function solveHypKepler
    ******************************************************/
    public double solveHypKepler(double x1, double x2)
    {
        double bigF, xacc=0.01;
        double fl,fh,xl,xh,swap,dxold,dx,f,df,temp;


        fl=x1-e*this.sinh(x1)+M;
        fh=x2-e*this.cosh(x1)+M;
        if (fl<0) {
            xl=x1;
            xh=x2;
        } else {
            xh=x1;
            xl=x2;
            swap=fl;
            fl=fh;
            fh=swap;
        }
        bigF=0.5*(x1+x2);
        dxold=Math.abs(x2-x1);
        dx=dxold;
        f=bigF-e*this.sinh(bigF)+M;
        df=1.-e*this.cosh(bigF);
        for (int j=1; j<=100; j++) {
            if ( ((bigF-xh)*df-f)*((bigF-xl)*df-f) > 0.0 || Math.abs(2.*f) > Math.abs(dxold*df) ) {
                dxold=dx;
                dx=0.5*(xh-xl);
                bigF=xl+dx;
                if (xl == bigF)  {return bigF;}
            } else {
                dxold=dx;
                dx=f/df;
                temp=bigF;
                bigF=bigF-dx;
                if (temp == bigF)  {return bigF;}
            }
            if (Math.abs(dx) < xacc)  {return bigF;}
            f=bigF-e*this.sinh(bigF)+M;
            df=1.-e*this.cosh(bigF);
            if (f < 0.)  {
                xl=bigF;
                fl=f;
            } else {
                xh=bigF;
                fh=f;
            }
        }
        System.out.println("solveHypKepler did too many iterations!");
        return bigF;

    }

    /*  function updateOrbit
    ******************************************************/
    public void updateOrbit(double ain, double ein)
    {

        a = ain;
        e = ein;
        tau = 0.0;
    }

    /* hyperbolic trig functions
    *****************************/
    public double sinh(double x) {
        double y;
        y=0.5*(Math.exp(x)-Math.exp(-x));
        return y;
    }
    public double cosh(double x) {
        double y;
        y=0.5*(Math.exp(x)+Math.exp(-x));
        return y;
    }
    public double tanh(double x) {
        double y;
        y=(Math.exp(x)-Math.exp(-x))/(Math.exp(x)+Math.exp(-x));
        return y;
    }
}
