package soottocfg.ast.Absyn; // Java Package generated by the BNF Converter.

public class XBody extends Body {
  public final ListLVarStatement listlvarstatement_;
  public XBody(ListLVarStatement p1) { listlvarstatement_ = p1; }

  public <R,A> R accept(soottocfg.ast.Absyn.Body.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof soottocfg.ast.Absyn.XBody) {
      soottocfg.ast.Absyn.XBody x = (soottocfg.ast.Absyn.XBody)o;
      return this.listlvarstatement_.equals(x.listlvarstatement_);
    }
    return false;
  }

  public int hashCode() {
    return this.listlvarstatement_.hashCode();
  }


}