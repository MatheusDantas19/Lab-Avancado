package concurrency.supwork;


interface Function {
  double fn(double x);
}

class OneMinusXsquared implements Function {
  public double fn (double x) {return 1-x*x;}
}

class OneMinusXcubed implements Function {
  public double fn (double x) {return 1-x*x*x;}
}

class XsquaredPlusPoint1 implements Function {
  public double fn (double x) {return x*x+0.1;}
}
