package soottocfg.ast.Absyn; // Java Package generated by the BNF Converter.

public class Asg extends Stm {
  public final String ident_;
  public final Exp exp_;
  public Asg(String p1, Exp p2) { ident_ = p1; exp_ = p2; }

  public <R,A> R accept(soottocfg.ast.Absyn.Stm.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof soottocfg.ast.Absyn.Asg) {
      soottocfg.ast.Absyn.Asg x = (soottocfg.ast.Absyn.Asg)o;
      return this.ident_.equals(x.ident_) && this.exp_.equals(x.exp_);
    }
    return false;
  }

  public int hashCode() {
    return 37*(this.ident_.hashCode())+this.exp_.hashCode();
  }


}