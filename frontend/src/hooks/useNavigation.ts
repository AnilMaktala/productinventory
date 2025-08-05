import { useNavigate, useLocation } from 'react-router-dom';

export const useNavigation = () => {
  const navigate = useNavigate();
  const location = useLocation();

  return {
    navigate,
    location,
    goTo: (path: string) => navigate(path),
    goBack: () => navigate(-1),
    currentPath: location.pathname,
  };
};
