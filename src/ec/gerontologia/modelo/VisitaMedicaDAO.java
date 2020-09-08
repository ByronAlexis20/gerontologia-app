package ec.gerontologia.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class VisitaMedicaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<VisitaMedica> getVisitas(Integer idPediodo) {
		List<VisitaMedica> resultado = new ArrayList<VisitaMedica>(); 
		Query query = getEntityManager().createNamedQuery("VisitaMedica.buscarTodos");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPediodo", idPediodo);
		resultado = (List<VisitaMedica>) query.getResultList();
		return resultado;
	}
}
