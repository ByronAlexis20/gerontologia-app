package ec.gerontologia.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class AsistenciaDAO extends ClaseDAO{
	
	@SuppressWarnings("unchecked")
	public List<Asistencia> getMatricula(Integer idMatricula, Integer idClase) {
		List<Asistencia> resultado = new ArrayList<Asistencia>();
		Query query = getEntityManager().createNamedQuery("Asistencia.buscarEncargadoCentro");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idMatricula", idMatricula);
		query.setParameter("idClase", idClase);
		resultado = (List<Asistencia>) query.getResultList();
		return resultado;
	}
}
