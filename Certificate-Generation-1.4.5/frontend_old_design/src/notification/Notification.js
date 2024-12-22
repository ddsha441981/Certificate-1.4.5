import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

export const showErrorNotification = (message, options = {}) => {
  toast.error(message, {
    position: options.position || "top-right",
    autoClose: options.autoClose || 5000,
    hideProgressBar: options.hideProgressBar !== undefined ? options.hideProgressBar : false,
    closeOnClick: options.closeOnClick !== undefined ? options.closeOnClick : true,
    pauseOnHover: options.pauseOnHover !== undefined ? options.pauseOnHover : true,
    draggable: options.draggable !== undefined ? options.draggable : true,
    progress: options.progress !== undefined ? options.progress : undefined,
    theme: options.theme || "light",
  });
};

export const showSuccessNotification = (message, options = {}) => {
  toast.success(message, {
    position: options.position || "top-right",
    autoClose: options.autoClose || 5000,
    hideProgressBar: options.hideProgressBar !== undefined ? options.hideProgressBar : false,
    closeOnClick: options.closeOnClick !== undefined ? options.closeOnClick : true,
    pauseOnHover: options.pauseOnHover !== undefined ? options.pauseOnHover : true,
    draggable: options.draggable !== undefined ? options.draggable : true,
    progress: options.progress !== undefined ? options.progress : undefined,
    theme: options.theme || "light",
  });
};

export const showInfoNotification = (message, options = {}) => {
  toast.info(message, {
    position: options.position || "top-right",
    autoClose: options.autoClose || 5000,
    hideProgressBar: options.hideProgressBar !== undefined ? options.hideProgressBar : false,
    closeOnClick: options.closeOnClick !== undefined ? options.closeOnClick : true,
    pauseOnHover: options.pauseOnHover !== undefined ? options.pauseOnHover : true,
    draggable: options.draggable !== undefined ? options.draggable : true,
    progress: options.progress !== undefined ? options.progress : undefined,
    theme: options.theme || "light",
  });
};

export const showWarningNotification = (message, options = {}) => {
  toast.warning(message, {
    position: options.position || "top-right",
    autoClose: options.autoClose || 5000,
    hideProgressBar: options.hideProgressBar !== undefined ? options.hideProgressBar : false,
    closeOnClick: options.closeOnClick !== undefined ? options.closeOnClick : true,
    pauseOnHover: options.pauseOnHover !== undefined ? options.pauseOnHover : true,
    draggable: options.draggable !== undefined ? options.draggable : true,
    progress: options.progress !== undefined ? options.progress : undefined,
    theme: options.theme || "light",
  });
};
