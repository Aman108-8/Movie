import React from 'react';
import { FiCheckCircle, FiMinusCircle, FiXCircle, FiHardDrive } from "react-icons/fi";

/*const PricingCard = ({ price, features, tier, highlight, buttonText }) => {

  const isStarter = tier === 'starter';
  const isPremium = tier === 'premium';
  const isGalaxy = tier === 'galaxy';

  // Colors
  const cardBorderColor = isPremium
    ? 'border-[#30EBFF]'
    : isGalaxy
    ? 'border-[#BD00FF]'
    : 'border-white/20';

  const textColor = isPremium
    ? 'text-[#30EBFF]'
    : isGalaxy
    ? 'text-[#BD00FF]'
    : 'text-white';

  const buttonBg = isGalaxy
    ? 'bg-[#BD00FF]'
    : isPremium
    ? 'bg-[#30EBFF]'
    : 'bg-white';

  const buttonTextColor = isGalaxy ? 'text-white' : 'text-black';

  return (
    <div
      className={`flex-1 mx-auto max-w-[350px] backdrop-blur-[20px] border ${cardBorderColor} rounded-3xl py-8 text-center text-white`}
      style={{
        boxShadow: `
          0 8px 50px rgba(255,255,255,0.15),
          inset 10px 10px 15px -10px rgba(255,255,255,0.15),
          inset -10px 10px 15px -10px rgba(255,255,255,0.15),
          inset 10px -10px 15px -10px rgba(255,255,255,0.15),
          inset -10px -10px 15px -10px rgba(255,255,255,0.15)
        `,
        backgroundColor: 'rgba(255,255,255,0.08)'
      }}
    >
      <div className="flex flex-col items-center mb-6 mt-4 px-8">
        <h3 className={`text-2xl p-1 tracking-wider uppercase ${textColor}`}>
          {isStarter
            ? 'MOVIE STREAMING'
            : isPremium
            ? 'PREMIUM PLAN'
            : 'ULTIMATE FAMILY'}
        </h3>
      </div>

      <div className="flex flex-col items-center mb-5 px-8">
        <h1 className={`text-5xl font-extrabold ${textColor}`}>{price}</h1>
        <p className={`text-xs uppercase tracking-widest mt-2 ${textColor}`}>
          {highlight}
        </p>
      </div>

      <div className='bg-[rgba(255,255,255,0.16)] py-2 font-bold text-xl'>
        <h2>14-days Free Trial</h2>
      </div>

      <div className="space-y-4 mb-5 text-left px-8 py-3">
        {features.map((feature, index) => (
          <div key={index} className="flex items-center gap-3">
            <span className="text-lg">
              {feature.icon === 'check' && <FiCheckCircle className="text-cyan-400" />}
              {feature.icon === 'minus' && <FiMinusCircle className="text-gray-500" />}
              {feature.icon === 'x' && <FiXCircle className="text-red-500" />}
              {feature.icon === 'drive' && <FiHardDrive className="text-gray-400" />}
            </span>

            <span className={`text-sm ${feature.bold ? 'font-semibold' : 'text-gray-400'}`}>
              {feature.text}
            </span>
          </div>
        ))}
      </div>

      <div className='px-8'>
        <button
          className={`w-full ${buttonBg} ${buttonTextColor} font-bold py-3 rounded-xl text-sm hover:opacity-90 transition`}
        >
          {buttonText}
        </button>
      </div>
    </div>
  );
};
*/

const PricingCard = ({ price, features, tier, highlight, buttonText }) => {
  const isStarter = tier === 'starter'
  const isPremium = tier === 'premium';
  const isGalaxy = tier === 'galaxy family';

  // Colors
  const cardBorderColor = isPremium
    ? 'border-[#30EBFF]'
    : isGalaxy
    ? 'border-[#BD00FF]'
    : 'border-white/20';

  const textColor = isPremium
    ? 'text-[#30EBFF]'
    : isGalaxy
    ? 'text-[#BD00FF]'
    : 'text-white';

  const buttonBg = isGalaxy
    ? 'bg-[#BD00FF]'
    : isPremium
    ? 'bg-[#30EBFF]'
    : 'bg-white';
    
    const buttonTextColor = isGalaxy ? 'text-white' : 'text-black';

    return(
      <>
      <div
      className={`flex-1 mx-auto max-w-[350px] backdrop-blur-[20px] border ${cardBorderColor} rounded-3xl py-8 text-center text-white`}
      style={{
        boxShadow: `
          0 8px 50px rgba(255,255,255,0.15),
          inset 10px 10px 15px -10px rgba(255,255,255,0.15),
          inset -10px 10px 15px -10px rgba(255,255,255,0.15),
          inset 10px -10px 15px -10px rgba(255,255,255,0.15),
          inset -10px -10px 15px -10px rgba(255,255,255,0.15)
        `,
        backgroundColor: 'rgba(255,255,255,0.08)'
      }}
    >
        <div className='flex flex-col items-center mb-8'>
          <h1 className={`text-2xl p-1 uppercase font-bold tracking-wider ${textColor}`}>{tier}</h1>
        </div>

        <div className='flex flex-col gap-y-2 mb-8'>
          <h2 className={`text-5xl ${textColor} font-extrabold`}>{price}</h2>
          <p className={`text-xs ${textColor} font-medium`}>{highlight}</p>
        </div>

        <div className='bg-[rgba(255,255,255,0.16)] py-2 font-bold text-xl'>
          <h2>14-days Free Trial</h2>
        </div>

        <div className=' space-y-4 mb-5 text-left px-8 py-3'>
          {features.map((feature, index)=>(
            <div key={index} className='flex items-center gap-3'>
                <span>
                  {feature.icon === 'check' && <FiCheckCircle className='text-cyan-400'/>}
                  {feature.icon === 'minus' && <FiMinusCircle className='text-gray-500'/>}
                  {feature.icon === 'x' && <FiXCircle className='text-red-500'/>}
                  {feature.icon === 'drive' && <FiHardDrive className='text-gray-400'/>}
                </span>

                <span className={`text-sm ${feature.bold ? 'font-semibold' : 'text-gray-400'}`}>
                  {feature.text}
                </span>
            </div>
          ))}
        </div>

        <div className='px-8'>
          <button className={`w-full ${buttonBg} ${buttonTextColor} font-bold py-3 rounded-xl text-sm hover:opcaity-10 transition`}>{buttonText}</button>
        </div>
      </div>
      </>
    )
}

 
const Pricing = () => {
  const plans = [
    {
      tier: 'starter',
      price: '$0',
      highlight: 'STARTER PACK',
      features: [
        { icon: 'x', text: 'Full HD' },
        { icon: 'check', text: 'Basic quality' },
        { icon: 'x', text: 'Ads free' },
        { icon: 'x', text: 'For individual' },
        { icon: 'x', text: 'Offline Downloads' },
        { icon: 'x', text: 'Early Access' },
      ],
      buttonText: 'Start Free',
    },
    {
      tier: 'premium',
      price: '$9.99/mo',
      highlight: 'PREMIUM PACK',
      features: [
        { icon: 'minus', text: 'Full HD' },
        { icon: 'check', text: 'High quality' },
        { icon: 'check', text: 'No ads' },
        { icon: 'check', text: 'For individual' },
        { icon: 'check', text: 'Early Access' },
        { icon: 'x', text: 'Offline Downloads' },
      ],
      buttonText: 'Subscribe',
    },
    {
      tier: 'galaxy family',
      price: '$19.99/mo',
      highlight: 'ULTIMATE FAMILY',
      features: [
        { icon: 'check', text: 'Ultimate quality' },
        { icon: 'check', text: '4K Ultra HD' },
        { icon: 'check', text: 'No ads' },
        { icon: 'check', text: 'Concurrent Screens' },
        { icon: 'drive', text: 'Offline Downloads', bold: true },
        { icon: 'check', text: 'Early Access' },
      ],
      buttonText: 'Subscribe',
    },
  ];

  return (
    <div className="flex justify-center items-center mt-[50px] px-4">
      <div className="flex flex-col md:flex-row gap-8 w-full max-w-[1300px]">
        {plans.map((plan) => (
          <PricingCard key={plan.tier} {...plan} />
        ))}
      </div>
    </div>
  );
};

export default Pricing;