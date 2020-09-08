package ec.gerontologia.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class GastoDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Gasto> getGasto(Integer idActividad) {
		List<Gasto> resultado = new ArrayList<Gasto>(); 
		Query query = getEntityManager().createNamedQuery("Gasto.buscarGasto");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idActividad", idActividad);
		resultado = (List<Gasto>) query.getResultList();
		return resultado;
	}
}
