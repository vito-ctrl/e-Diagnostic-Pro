<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr" class="h-full bg-slate-50">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.title != null ? param.title : 'Télé-Expertise Médicale'}</title>
    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    colors: {
                        brand: {
                            50: '#f0f9ff',
                            100: '#e0f2fe',
                            500: '#0284c7',
                            600: '#0369a1',
                            700: '#075985',
                        }
                    }
                }
            }
        }
    </script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="h-full flex flex-col text-slate-800">

<c:if test="${not empty sessionScope.user}">
    <!-- Navigation Bar -->
    <header class="bg-white border-b border-slate-200 sticky top-0 z-50 shadow-sm">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="flex justify-between h-16">
                <div class="flex items-center space-x-3">
                    <div class="w-10 h-10 rounded-xl bg-sky-600 flex items-center justify-center text-white shadow-md shadow-sky-200">
                        <i class="fa-solid fa-notes-medical text-lg"></i>
                    </div>
                    <div>
                        <span class="text-lg font-bold text-slate-900 tracking-tight">Télé-Expertise</span>
                        <span class="text-xs px-2 py-0.5 rounded-full font-medium ml-2 
                            <c:choose>
                                <c:when test="${sessionScope.user.role == 'INFIRMIER'}">bg-emerald-100 text-emerald-800</c:when>
                                <c:when test="${sessionScope.user.role == 'GENERALISTE'}">bg-sky-100 text-sky-800</c:when>
                                <c:when test="${sessionScope.user.role == 'SPECIALISTE'}">bg-purple-100 text-purple-800</c:when>
                                <c:otherwise>bg-amber-100 text-amber-800</c:otherwise>
                            </c:choose>">
                            ${sessionScope.user.role.libelle}
                        </span>
                    </div>
                </div>

                <!-- Navigation Links depending on Role -->
                <nav class="hidden md:flex items-center space-x-1">
                    <c:choose>
                        <c:when test="${sessionScope.user.role == 'INFIRMIER'}">
                            <a href="${pageContext.request.contextPath}/infirmier/patients" class="px-3 py-2 rounded-lg text-sm font-medium hover:bg-slate-100 text-slate-700">
                                <i class="fa-solid fa-users mr-1.5 text-slate-400"></i> Patients du jour
                            </a>
                            <a href="${pageContext.request.contextPath}/infirmier/recherche" class="px-3 py-2 rounded-lg text-sm font-medium hover:bg-slate-100 text-slate-700">
                                <i class="fa-solid fa-magnifying-glass mr-1.5 text-slate-400"></i> Rechercher / Accueillir
                            </a>
                            <a href="${pageContext.request.contextPath}/infirmier/nouveau" class="px-3 py-2 rounded-lg text-sm font-medium bg-emerald-600 hover:bg-emerald-700 text-white ml-2">
                                <i class="fa-solid fa-user-plus mr-1.5"></i> Nouveau Patient
                            </a>
                        </c:when>

                        <c:when test="${sessionScope.user.role == 'GENERALISTE'}">
                            <a href="${pageContext.request.contextPath}/generaliste/file-attente" class="px-3 py-2 rounded-lg text-sm font-medium hover:bg-slate-100 text-slate-700">
                                <i class="fa-solid fa-list-ol mr-1.5 text-slate-400"></i> File d'attente
                            </a>
                            <a href="${pageContext.request.contextPath}/generaliste/consultations" class="px-3 py-2 rounded-lg text-sm font-medium hover:bg-slate-100 text-slate-700">
                                <i class="fa-solid fa-stethoscope mr-1.5 text-slate-400"></i> Mes Consultations
                            </a>
                        </c:when>

                        <c:when test="${sessionScope.user.role == 'SPECIALISTE'}">
                            <a href="${pageContext.request.contextPath}/specialiste/expertises" class="px-3 py-2 rounded-lg text-sm font-medium hover:bg-slate-100 text-slate-700">
                                <i class="fa-solid fa-inbox mr-1.5 text-slate-400"></i> Demandes d'expertise
                            </a>
                            <a href="${pageContext.request.contextPath}/specialiste/creneaux" class="px-3 py-2 rounded-lg text-sm font-medium hover:bg-slate-100 text-slate-700">
                                <i class="fa-solid fa-calendar-check mr-1.5 text-slate-400"></i> Mes Créneaux
                            </a>
                            <a href="${pageContext.request.contextPath}/specialiste/profil" class="px-3 py-2 rounded-lg text-sm font-medium hover:bg-slate-100 text-slate-700">
                                <i class="fa-solid fa-user-doctor mr-1.5 text-slate-400"></i> Profil & Tarif
                            </a>
                        </c:when>

                        <c:when test="${sessionScope.user.role == 'ADMIN'}">
                            <a href="${pageContext.request.contextPath}/admin/staff" class="px-3 py-2 rounded-lg text-sm font-medium hover:bg-slate-100 text-slate-700">
                                <i class="fa-solid fa-users-gear mr-1.5 text-slate-400"></i> Gestion du Staff
                            </a>
                            <a href="${pageContext.request.contextPath}/infirmier/patients" class="px-3 py-2 rounded-lg text-sm font-medium hover:bg-slate-100 text-slate-700">
                                <i class="fa-solid fa-hospital-user mr-1.5 text-slate-400"></i> Vue Infirmier
                            </a>
                            <a href="${pageContext.request.contextPath}/generaliste/file-attente" class="px-3 py-2 rounded-lg text-sm font-medium hover:bg-slate-100 text-slate-700">
                                <i class="fa-solid fa-stethoscope mr-1.5 text-slate-400"></i> Vue Généraliste
                            </a>
                        </c:when>
                    </c:choose>
                </nav>

                <!-- User profile & Logout -->
                <div class="flex items-center space-x-3">
                    <div class="text-right hidden sm:block">
                        <div class="text-sm font-semibold text-slate-800">${sessionScope.user.nomComplet}</div>
                        <div class="text-xs text-slate-500">${sessionScope.user.email}</div>
                    </div>
                    <a href="${pageContext.request.contextPath}/logout" class="p-2 text-slate-500 hover:text-red-600 hover:bg-red-50 rounded-lg transition" title="Déconnexion">
                        <i class="fa-solid fa-arrow-right-from-bracket text-lg"></i>
                    </a>
                </div>
            </div>
        </div>
    </header>
</c:if>

<main class="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-6">
