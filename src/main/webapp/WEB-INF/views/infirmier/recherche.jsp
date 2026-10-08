<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Accueil Patient - Étape 1 Recherche" />
</jsp:include>

<div class="max-w-4xl mx-auto space-y-6">
    <!-- Header -->
    <div class="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm">
        <h1 class="text-2xl font-bold text-slate-900">US1 : Accueil du patient &bull; Étape 1 : Recherche</h1>
        <p class="mt-1 text-sm text-slate-500">
            Recherchez si le patient possède déjà un dossier (par Nom, Prénom ou N° Sécurité Sociale).
        </p>

        <!-- Search Form -->
        <form action="${pageContext.request.contextPath}/infirmier/recherche" method="GET" class="mt-6 flex gap-3">
            <div class="relative flex-1">
                <span class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                    <i class="fa-solid fa-magnifying-glass"></i>
                </span>
                <input type="text" name="query" value="${query}" required
                       placeholder="Rechercher par N° Sécu (ex: AB123456) ou Nom..."
                       class="w-full pl-10 pr-4 py-2.5 border border-slate-300 rounded-xl text-slate-900 focus:outline-none focus:ring-2 focus:ring-sky-500 text-sm" />
            </div>
            <button type="submit" class="px-5 py-2.5 bg-sky-600 hover:bg-sky-700 text-white rounded-xl text-sm font-semibold shadow-sm transition">
                Rechercher
            </button>
            <a href="${pageContext.request.contextPath}/infirmier/nouveau"
               class="px-5 py-2.5 bg-emerald-600 hover:bg-emerald-700 text-white rounded-xl text-sm font-semibold shadow-sm flex items-center space-x-1.5 transition">
                <i class="fa-solid fa-user-plus"></i>
                <span>Nouveau Patient</span>
            </a>
        </form>
    </div>

    <!-- Search Results -->
    <c:if test="${not empty query}">
        <div class="bg-white rounded-2xl border border-slate-200 shadow-sm p-6">
            <h2 class="text-lg font-bold text-slate-900 mb-4">
                Résultats de la recherche pour "${query}"
            </h2>

            <c:choose>
                <c:when test="${empty resultats}">
                    <div class="text-center py-8">
                        <i class="fa-solid fa-user-xmark text-4xl text-slate-300 mb-2"></i>
                        <p class="text-slate-600 font-medium">Aucun patient trouvé correspondant à ces critères.</p>
                        <p class="text-xs text-slate-400 mt-1">Vous pouvez créer directement le dossier de ce nouveau patient :</p>
                        <div class="mt-4">
                            <a href="${pageContext.request.contextPath}/infirmier/nouveau"
                               class="inline-flex items-center px-4 py-2 bg-emerald-600 hover:bg-emerald-700 text-white rounded-xl text-sm font-semibold shadow-sm transition">
                                <i class="fa-solid fa-user-plus mr-1.5"></i> Créer la fiche patient (Étape 2b)
                            </a>
                        </div>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="divide-y divide-slate-100">
                        <c:forEach var="p" items="${resultats}">
                            <div class="py-4 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
                                <div>
                                    <div class="flex items-center space-x-2">
                                        <h3 class="font-bold text-slate-900 text-base">${p.nomComplet}</h3>
                                        <span class="px-2.5 py-0.5 rounded-full text-xs font-mono font-medium bg-slate-100 text-slate-800 border border-slate-200">
                                            ${p.numSecu}
                                        </span>
                                    </div>
                                    <div class="text-xs text-slate-500 mt-1 space-x-3">
                                        <span><i class="fa-solid fa-calendar mr-1"></i> Né(e) le : ${p.dateNaissance}</span>
                                        <span><i class="fa-solid fa-phone mr-1"></i> ${p.telephone}</span>
                                        <span><i class="fa-solid fa-shield-halved mr-1"></i> Mutuelle : ${p.mutuelle}</span>
                                    </div>
                                    <div class="text-xs text-slate-400 mt-1">
                                        Antécédents : ${not empty p.antecedents ? p.antecedents : 'Aucun'} &bull;
                                        Allergies : ${not empty p.allergies ? p.allergies : 'Aucune'}
                                    </div>
                                </div>
                                <div>
                                    <a href="${pageContext.request.contextPath}/infirmier/existant?id=${p.id}"
                                       class="inline-flex items-center px-4 py-2 bg-sky-600 hover:bg-sky-700 text-white rounded-xl text-sm font-semibold shadow-sm transition">
                                        <i class="fa-solid fa-heart-pulse mr-1.5"></i> Saisir signes vitaux & file d'attente
                                    </a>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </c:if>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
