package ec.gerontologia.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class ClasesDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Clase> getClasePorPeriodo(Integer idPeriodo,String value) {
		List<Clase> resultado = new ArrayList<Clase>();
		String patron = value;
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		
		Query query = getEntityManager().createNamedQuery("Clase.buscarPorPeriodo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPeriodo", idPeriodo);
		query.setParameter("patron", patron);
		resultado = (List<Clase>) query.getResultList();
		return resultado;
	}
}
