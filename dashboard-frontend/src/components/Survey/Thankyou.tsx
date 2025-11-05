import React from "react";

interface ThankYouProps {
  onFinish?: () => void; 
}

const ThankYou: React.FC<ThankYouProps> = ({ onFinish }) => {
  return (
    <div className="flex flex-col items-center justify-center h-[60vh] text-center animate-fadeIn">
      <div className="flex flex-col items-center">
        <div className="bg-green-100 text-green-600 rounded-full w-20 h-20 flex items-center justify-center mb-6 shadow-md animate-bounce">
          <span className="text-4xl">✓</span>
        </div>
        <h2 className="text-2xl font-semibold mb-2">Thank you for your participation!</h2>
        <p className="text-gray-600 mb-6">
          Your responses have been recorded successfully.
        </p>

        
        {onFinish && (
          <button
            onClick={onFinish}
            className="mt-4 px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition"
          >
            Go Back
          </button>
        )}
      </div>
    </div>
  );
};

export default ThankYou;
