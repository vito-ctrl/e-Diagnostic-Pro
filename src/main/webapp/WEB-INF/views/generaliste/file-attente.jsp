<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="File d'attente - Médecin Généraliste" />
</jsp:include>

<div class="space-y-6">
    <div class="sm:flex sm:items-center sm:justify-between bg-white p-6 rounded-2xl border border-slate-200 shadow-sm">
        <div>
            <h1 class="text-2xl font-bold text-slate-900">File d'attente des consultations</h1>
            <p class="mt-1 text-sm text-slate-500">
                Patients accueillis par l'infirmier en attente de consultation médicale
            </p>
        </div>
        <div class="mt-4 sm:mt-0 flex items-center space-x-2">
            <span class="inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold bg-sky-100 text-sky-800 border border-sky-200">
                <i class="fa-solid fa-users mr-1.5"></i> ${fileActive.size()} patient(s) en attente
            </span>
            <a href="${pageContext.request.contextPath}/generaliste/consultations"
               class="px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-xl text-sm font-semibold transition">
                <i class="fa-solid fa-folder-open mr-1"></i> Historique consultations
            </a>
        </div>
    </div>

    <!-- Active Queue Cards/Table -->
    <div class="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
        <div class="overflow-x-auto">
            <table class="min-w-full divide-y divide-slate-200 text-left text-sm">
                <thead class="bg-slate-50 text-slate-500 uppercase font-semibold text-xs tracking-wider">
                    <tr>
                        <th class="px-6 py-4">Arrivée</th>
                        <th class="px-6 py-4">Patient</th>
                        <th class="px-6 py-4">N° Sécurité Sociale</th>
                        <th class="px-6 py-4">Signes Vitaux Recueillis</th>
                        <th class="px-6 py-4 text-right">Action</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-slate-100 bg-white">
                    <c:choose>
                        <c:when test="${empty fileActive}">
                            <tr>
                                <td colspan="5" class="px-6 py-12 text-center text-slate-400">
                                    <i class="fa-solid fa-mug-hot text-4xl mb-3 text-slate-300"></i>
                                    <p class="font-medium text-slate-600">Aucun patient en file d'attente pour le moment.</p>
                                    <p class="text-xs text-slate-400 mt-1">Les patients apparaîtront dès leur accueil par l'infirmier.</p>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="item" items="${fileActive}">
                                <tr class="hover:bg-slate-50/80 transition">
                                    <td class="px-6 py-4 whitespace-nowrap font-medium text-slate-900">
                                        <div class="flex items-center space-x-2">
                                            <i class="fa-regular fa-clock text-sky-600"></i>
                                            <span>${item.heureArrivee.toLocalTime().toString().substring(0, 5)}</span>
                                        </div>
                                    </td>
                                    <td class="px-6 py-4">
                                        <div class="font-semibold text-slate-900">${item.patient.nomComplet}</div>
                                        <div class="text-xs text-slate-500">
                                            Né(e) : ${item.patient.dateNaissance} &bull; Mutuelle : ${item.patient.mutuelle}
                                        </div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-mono font-medium bg-slate-100 text-slate-800 border border-slate-200">
                                            ${item.patient.numSecu}
                                        </span>
                                    </td>
                                    <td class="px-6 py-4">
                                        <c:choose>
                                            <c:when test="${not empty item.signesVitaux}">
                                                <div class="text-xs space-y-1">
                                                    <div>
                                                        <span class="font-semibold text-slate-700">TA:</span> ${item.signesVitaux.tensionArterielle}
                                                        &bull; <span class="font-semibold text-slate-700">FC:</span> ${item.signesVitaux.frequenceCardiaque} bpm
                                                    </div>
                                                    <div class="text-slate-500">
                                                        <span class="font-semibold text-slate-700">T°:</span> ${item.signesVitaux.temperature} °C
                                                        &bull; <span class="font-semibold text-slate-700">FR:</span> ${item.signesVitaux.frequenceRespiratoire} cpm
                                                        <c:if test="${item.signesVitaux.poids != null}">
                                                            &bull; ${item.signesVitaux.poids} kg
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-xs text-slate-400 italic">Non renseignés</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="px-6 py-4 text-right whitespace-nowrap">
                                        <a href="${pageContext.request.contextPath}/generaliste/nouvelle-consultation?fileId=${item.id}"
                                           class="inline-flex items-center px-4 py-2 bg-sky-600 hover:bg-sky-700 text-white rounded-xl text-xs font-semibold shadow-sm transition">
                                            <i class="fa-solid fa-stethoscope mr-1.5"></i> Consulter
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
