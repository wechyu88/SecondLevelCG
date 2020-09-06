package analysisData;

public class Bond {
	Atom a;
	Atom b;
	int type;
	int id;
	public Bond(Atom a,Atom b) {
		this.a=a;
		this.b=b;
	}
	public void settype(int type) {
		this.type = type;
	}
	public void setid(int id) {
		this.id=id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		result = prime * result + type;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bond other = (Bond) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}
