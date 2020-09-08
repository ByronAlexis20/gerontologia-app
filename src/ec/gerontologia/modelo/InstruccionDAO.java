package ec.gerontologia.modelo;

import java.util.List;

import javax.persistence.Query;

public class InstruccionDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Instruccion> getInstrucciones(String value) {
		List<Instruccion> resultado; 
		String patron = value;
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Instruccion.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Instruccion>) query.getResultList();
		return resultado;
	}
}
