package soottocfg.ast.Absyn; // Java Package generated by the BNF Converter.

public class Cnp extends SpecExp {
  public final SpecExpNP specexpnp_;
  public Cnp(SpecExpNP p1) { specexpnp_ = p1; }

  public <R,A> R accept(soottocfg.ast.Absyn.SpecExp.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof soottocfg.ast.Absyn.Cnp) {
      soottocfg.ast.Absyn.Cnp x = (soottocfg.ast.Absyn.Cnp)o;
      return this.specexpnp_.equals(x.specexpnp_);
    }
    return false;
  }

  public int hashCode() {
    return this.specexpnp_.hashCode();
  }


}