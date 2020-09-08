package ec.gerontologia.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class ProvinciaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Provincia> getProvincias() {
		List<Provincia> resultado = new ArrayList<Provincia>(); 
		Query query = getEntityManager().createNamedQuery("Provincia.buscarTodos");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Provincia>) query.getResultList();
		return resultado;
	}
}
